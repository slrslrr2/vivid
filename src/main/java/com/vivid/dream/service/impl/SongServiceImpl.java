package com.vivid.dream.service.impl;

import com.vivid.dream.enums.GenreEnum;
import com.vivid.dream.mapper.SongMapper;
import com.vivid.dream.model.SongDetailVo;
import com.vivid.dream.model.SongLyricsVo;
import com.vivid.dream.model.SongVo;
import com.vivid.dream.service.SongService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongMapper songMapper;

    @Resource(name = "songTaskExecutor")
    private TaskExecutor songTaskExecutor;

    @Resource(name = "songDetailTaskExecutor")
    private TaskExecutor songDetailTaskExecutor;

    @Resource(name = "songLyricsTaskExecutor")
    private TaskExecutor songLyricsTaskExecutor;

//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "1 * * * * ?")
    public void getSongInfo(){
        // TODO: Redis Lock
//        boolean lock = false;
        try {
//            lock = RedisLockUtils.tryLock(redisTemplate, NftConstants.MODIFY_START_PROJECT_LOCK, 10000);
//            if (!lock) {
//                return;
//            }
            // selectRecentDataBySong 최근 일자 등록 되어있다면 return;

            Document doc = Jsoup.connect("https://www.melon.com/chart/index.htm").get();
            Elements trElement = doc.select("div#tb_list table tbody tr");

            for (Element tr : trElement) {
                createSong(tr)
                    .thenCompose(songVo -> createSongDetail(songVo))
                    .thenAccept(songVo -> createSongLyrics(songVo))
                    .exceptionally(throwable -> {
                        System.out.println(throwable.getMessage());
                        return null;
                    });
            }

        } catch (Exception e) {
//            log.warn("fixedProjectStatusModify error {}", e.getMessage(), e);
        } finally {
//            if (lock) {
//                RedisLockUtils.releaseLock(redisTemplate, NftConstants.MODIFY_START_PROJECT_LOCK);
//            }
        }
    }

    private CompletableFuture<SongVo> createSong(Element tr) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Long melonId = Long.parseLong(tr.attr("data-song-no"));
                Integer rank = Integer.parseInt(tr.select("span.rank").first().text());
                String songName = tr.select("div.wrap_song_info .rank01 a").text();
                String artist = tr.select("div.wrap_song_info .rank02 .checkEllipsis a").text();

                SongVo song = SongVo.builder()
                        .melonId(melonId)
                        .songName(songName)
                        .artist(artist)
                        .rank(rank)
                        .build();

                int insertCount = songMapper.insertSong(song);
                if (insertCount != 1) {
                    throw new RuntimeException("Failed to insert song: " + song);
                }

                return song;
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }, songTaskExecutor);
    };

    private CompletableFuture<SongVo> createSongDetail(SongVo song) throws RuntimeException{
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId=" + song.getMelonId()).get();
                Elements metaElemet = songDetailDoc.select("form#downloadfrm .meta .list dd");
                String albumName = metaElemet.get(0).select("a").text();
                String releaseDate = metaElemet.get(1).text();
                GenreEnum genre = GenreEnum.findGenreEnumByDescription(metaElemet.get(2).text());
                String imgUrl = songDetailDoc.select("form#downloadfrm .image_typeAll img").attr("src");
                SongDetailVo songDetail = SongDetailVo.builder()
                        .melonId(song.getMelonId())
                        .albumName(albumName)
                        .imgUrl(imgUrl)
                        .releaseDate(releaseDate)
                        .genre(genre.getValue())
                        .build();

                int insertCount = songMapper.insertSongDetail(songDetail);
                if (insertCount != 1) {
                    throw new RuntimeException("Failed to insert song detail: " + songDetail);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return song;
        }, songDetailTaskExecutor);
    }

    private CompletableFuture<Void> createSongLyrics(SongVo song) {
        return CompletableFuture.runAsync(() -> {
            try{
                Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId="+ song.getMelonId()).get();
                String lyrics = songDetailDoc.select("div#d_video_summary").text();
                SongLyricsVo songLyrics = SongLyricsVo.builder()
                        .melonId(song.getMelonId())
                        .songName(song.getSongName())
                        .artist(song.getArtist())
                        .lyrics(lyrics)
                        .build();

                int result = songMapper.insertSongLyrics(songLyrics);
                if (result != 1) {
                    throw new RuntimeException("Error inserting song lyrics");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, songLyricsTaskExecutor);
    }
}
