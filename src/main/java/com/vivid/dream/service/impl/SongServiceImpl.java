package com.vivid.dream.service.impl;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.entity.Song;
import com.vivid.dream.entity.SongDetail;
import com.vivid.dream.entity.SongLyrics;
import com.vivid.dream.enums.GenreEnum;
import com.vivid.dream.mapper.SongMapper;
import com.vivid.dream.service.SongService;
import com.vivid.dream.vo.response.ResponseSong;
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

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongMapper songMapper;

    @Resource(name = "songTaskExecutor")
    private TaskExecutor songTaskExecutor;

        @Scheduled(cron = "0 5 12 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
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
        Song song = createSong(tr);
        createSongDetail(song.getMelonId());
        createSongLyrics(song);
    }

    @Override
    public List<ResponseSong> getSongList(String date) {
        List<Song> songList = songMapper.selectSongList(date);
        List<ResponseSong> result = new ArrayList<>();
        for (Song songVo : songList) {
            ResponseSong song = ResponseSong.builder()
                    .id(songVo.getId())
                    .melonId(songVo.getMelonId())
                    .songName(songVo.getSongName())
                    .artist(songVo.getArtist())
                    .ranking(songVo.getRanking())
                    .createDate(songVo.getCreateDate()).build();
            result.add(song);
        }
        return result;
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
            Optional<SongDetail> preSongDetail = songMapper.selectSongDetail(melonId);
            if(preSongDetail.isPresent()) return;

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
                    .build();

            int result = songMapper.insertSongDetail(songDetail);
            if (result != 1) {
                log.warn("createSongDetail Failed | melonId: {}", melonId);
            }
        } catch (Exception e) {
            log.warn("createSongDetail Failed error {}", e.getMessage(), e);
        }
    }

    private void createSongLyrics(Song song){
        try {
            Optional<SongLyrics> preSongLyrics = songMapper.selectSongLyrics(song.getMelonId());
            if(preSongLyrics.isPresent()) return;

            Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId=" + song.getMelonId()).get();
            String lyrics = songDetailDoc.select("div#d_video_summary").text();

            SongLyrics songLyrics = SongLyrics.builder()
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
