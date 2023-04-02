package com.vivid.dream.service.impl;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.enums.GenreEnum;
import com.vivid.dream.mapper.SongMapper;
import com.vivid.dream.model.SongDetailVo;
import com.vivid.dream.model.SongLyricsVo;
import com.vivid.dream.model.SongVo;
import com.vivid.dream.service.SongService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongMapper songMapper;

    @Resource(name = "songTaskExecutor")
    private TaskExecutor songTaskExecutor;

    //    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "1 * * * * ?")
    public void crawlingMelonChart(){
        try {
            Integer nowDateCountBySong = songMapper.selectNowDataCount().orElse(0);
            if(nowDateCountBySong > 0)
                log.warn("regist top 100 song {} {}", LocalDateTime.now(), ResultCode.TODAY_SONG_TOP100_IS_EXIST.getDesc());

            Document doc = Jsoup.connect("https://www.melon.com/chart/index.htm").get();
            Elements trElement = doc.select("div#tb_list table tbody tr");
            SongService songService = (SongService) AopContext.currentProxy();

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

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createSongByMelonChart(Element tr) {
        SongVo song = createSong(tr);
        createSongDetail(song.getMelonId());
        createSongLyrics(song);
    }

    private SongVo createSong(Element tr){
        SongVo song = null;
        try {
            Long melonId = Long.parseLong(tr.attr("data-song-no"));
            Integer rank = Integer.parseInt(tr.select("span.rank").first().text());
            String songName = tr.select("div.wrap_song_info .rank01 a").text();
            String artist = tr.select("div.wrap_song_info .rank02 .checkEllipsis a").text();

            song = SongVo.builder()
                    .melonId(melonId)
                    .songName(songName)
                    .artist(artist)
                    .rank(rank)
                    .build();

            int result = songMapper.insertSong(song);
            if (result != 1) {
                log.warn("createSong Failed | melonId: {}", song.getMelonId());
            }

        } catch (Exception e) {
            log.warn("createSong Failed error {}", e.getMessage(), e);
        }

        return song;
    }

    private void createSongDetail(Long melonId){
        try{
            Optional<SongDetailVo> preSongDetail = songMapper.selectSongDetail(melonId);
            if(preSongDetail.isPresent()) return;

            Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId=" + melonId).get();
            Elements metaElement = songDetailDoc.select("form#downloadfrm .meta .list dd");
            String albumName = metaElement.get(0).select("a").text();
            String releaseDate = metaElement.get(1).text();
            GenreEnum genre = GenreEnum.findGenreEnumByDescription(metaElement.get(2).text());
            String imgUrl = songDetailDoc.select("form#downloadfrm .image_typeAll img").attr("src");
            SongDetailVo songDetail = SongDetailVo.builder()
                    .melonId(melonId)
                    .albumName(albumName)
                    .imgUrl(imgUrl)
                    .releaseDate(releaseDate)
                    .genre(genre.getValue())
                    .build();

            int result = songMapper.insertSongDetail(songDetail);
            if (result != 1) {
                log.warn("createSongDetail Failed | melonId: {}", melonId);
            }
        } catch (Exception e) {
            log.warn("createSongDetail Failed error {}", e.getMessage(), e);
        }
    }

    private void createSongLyrics(SongVo song){
        try {
            Optional<SongLyricsVo> preSongLyrics = songMapper.selectSongLyrics(song.getMelonId());
            if(preSongLyrics.isPresent()) return;

            Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId=" + song.getMelonId()).get();
            String lyrics = songDetailDoc.select("div#d_video_summary").text();
            SongLyricsVo songLyrics = SongLyricsVo.builder()
                    .melonId(song.getMelonId())
                    .songName(song.getSongName())
                    .artist(song.getArtist())
                    .lyrics(lyrics)
                    .build();

            int result = songMapper.insertSongLyrics(songLyrics);
            if (result != 1) {
                log.warn("createSongLyrics Failed | melonId: {}", song.getMelonId());
            }
        } catch (Exception e) {
            log.warn("createSongLyrics Failed error {}", e.getMessage(), e);
        }
    }
}
