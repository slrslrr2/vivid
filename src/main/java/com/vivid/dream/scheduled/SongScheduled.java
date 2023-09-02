package com.vivid.dream.scheduled;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.config.handler.exception.WebException;
import com.vivid.dream.entity.Song;
import com.vivid.dream.entity.SongDetail;
import com.vivid.dream.entity.SongLyrics;
import com.vivid.dream.enums.GenreEnum;
import com.vivid.dream.repository.SongDetailRepository;
import com.vivid.dream.repository.SongLyricsRepository;
import com.vivid.dream.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class SongScheduled {
    private final SongRepository songRepository;
    private final SongDetailRepository songDetailRepository;
    private final SongLyricsRepository songLyricsRepository;

    @Resource(name = "songTaskExecutor")
    private TaskExecutor songTaskExecutor;

    @Scheduled(cron = "0 5 12 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
    public void crawlingMelonChart(){
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfDay = now.with(LocalTime.MIN);

            Long nowDateCountBySong = songRepository.countByCreateDateAfter(startOfDay);
            if(nowDateCountBySong > 0){
                log.warn("regist top 100 song {} {}", LocalDateTime.now(), ResultCode.TODAY_SONG_TOP100_IS_EXIST.getDesc());
                return;
            }

            Document doc = Jsoup.connect("https://www.melon.com/chart/index.htm").get();
            Elements trElement = doc.select("div#tb_list table tbody tr");
            SongScheduled songService = (SongScheduled) AopContext.currentProxy();

            for (Element tr : trElement) {
                CompletableFuture.runAsync(() -> {
                    try {
                        songService.createSongByMelonChart(tr);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }, songTaskExecutor)
                .exceptionally(throwable -> {
                    log.warn("crawlingMelonChart Failed error {}", throwable.getMessage(), throwable);
                    return null;
                });
            }

        } catch (Exception e) {
            log.warn("crawlingMelonChart Failed error {}", e.getMessage(), e);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void createSongByMelonChart(Element tr) {
        Song song = createSong(tr);
        createSongDetail(song.getMelonId());
        createSongLyrics(song);
    }

    private Song createSong(Element tr){
        Song song = null;
        try {
            Long melonId = Long.parseLong(tr.attr("data-song-no"));
            Integer ranking = Integer.parseInt(tr.select("span.rank").first().text());
            String songName = tr.select("div.wrap_song_info .rank01 a").text();
            String artist = tr.select("div.wrap_song_info .rank02 .checkEllipsis a").text();

            song = Song.builder()
                    .melonId(melonId)
                    .songName(songName)
                    .artist(artist)
                    .ranking(ranking)
                    .createDate(LocalDateTime.now())
                    .build();

            songRepository.save(song);
        } catch (Exception e) {
            log.warn("createSong Failed error {}", e.getMessage(), e);
            throw new WebException(ResultCode.CREATE_SONG_BY_SCHEDULED_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return song;
    }

    private void createSongDetail(Long melonId){
        try{
            List<SongDetail> allByMelonId = songDetailRepository.findAllByMelonId(melonId);
            if(allByMelonId.size() > 0) return;

            Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId=" + melonId).get();
            Elements metaElement = songDetailDoc.select("form#downloadfrm .meta .list dd");
            String albumName = metaElement.get(0).select("a").text();
            String releaseDate = metaElement.get(1).text();
            GenreEnum genre = GenreEnum.findGenreEnumByDescription(metaElement.get(2).text());
            String imgUrl = songDetailDoc.select("form#downloadfrm .image_typeAll img").attr("src");

            SongDetail songDetail = SongDetail.builder()
                    .melonId(melonId)
                    .albumName(albumName)
                    .imgUrl(imgUrl)
                    .releaseDate(releaseDate)
                    .genre(genre.getValue())
                    .createDate(LocalDateTime.now())
                    .build();
            songDetailRepository.save(songDetail);

        } catch (Exception e) {
            log.warn("createSongDetail Failed error {}", e.getMessage(), e);
            throw new WebException(ResultCode.CREATE_SONG_DETAIL_BY_SCHEDULED_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createSongLyrics(Song song){
        try {
            List<SongLyrics> allByMelonId = songLyricsRepository.findAllByMelonId(song.getMelonId());
            if(allByMelonId.size() > 0) return;
            Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId=" + song.getMelonId()).get();
            String lyrics = songDetailDoc.select("div#d_video_summary").text();

            SongLyrics songLyrics = SongLyrics.builder()
                    .melonId(song.getMelonId())
                    .songName(song.getSongName())
                    .artist(song.getArtist())
                    .lyrics(lyrics)
                    .createDate(LocalDateTime.now())
                    .build();

            songLyricsRepository.save(songLyrics);
        } catch (Exception e) {
            log.warn("createSongLyrics Failed error {}", e.getMessage(), e);
            throw new WebException(ResultCode.CREATE_SONG_LYRICS_BY_SCHEDULED_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
