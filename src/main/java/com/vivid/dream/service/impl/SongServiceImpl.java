package com.vivid.dream.service.impl;

import com.vivid.dream.model.SongDetailVo;
import com.vivid.dream.model.SongVo;
import com.vivid.dream.service.SongService;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Service
public class SongServiceImpl implements SongService {
    //    private final TestMapper testMapper;

    @Resource(name = "getSongDetailTaskExecutor")
    private TaskExecutor getSongDetailTaskExecutor;

//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "* 47 * * * ?")
    public void getSongInfo(){
//        boolean lock = false;
        try {
//            lock = RedisLockUtils.tryLock(redisTemplate, NftConstants.MODIFY_START_PROJECT_LOCK, 10000);
//            if (!lock) {
//                return;
//            }
            // selectRecentDataBySong 최근 일자 등록 되어있다면 return;

            Document doc = Jsoup.connect("https://www.melon.com/chart/index.htm").get();
            Elements trElement = doc.select("div#tb_list table tbody tr");

            HashSet<String> strings = new HashSet<>();
            for (Element tr : trElement) {
                SongVo song = createSong(tr);

//                CompletableFuture.runAsync(() -> {
                    try{
                        Document songDetailDoc = Jsoup.connect("https://www.melon.com/song/detail.htm?songId="+song.getMelonId()).get();
                        Elements metaElemet = songDetailDoc.select("form#downloadfrm .meta .list dd");
                        String albumName = metaElemet.get(0).select("a").text();
                        String releaseDate = metaElemet.get(1).text();
                        String genre = metaElemet.get(2).text();

                        strings.add(genre);

//                        SongDetailVo.builder()
//                                .melonId(song.getMelonId())


                        //melon_id bigint DEFAULT 0 COMMENT 'melon id',
                        //album_name VARCHAR(100) NOT NULL COMMENT '앨범명',
                        //image_url VARCHAR(200) NOT NULL COMMENT '커버 이미지 URL',
                        //image_thumb_url VARCHAR(200) NOT NULL COMMENT '커버 이미지 썸네일 URL',
                        //release_date VARCHAR(10) NOT NULL COMMENT '발매일',
                        //genre tinyint(4) NOT NULL COMMENT '장르',
                        //create_date DATE NOT NULL,
                        //PRIMARY KEY (`id`),
                        //UNIQUE KEY `melon_id` (`melon_id`)

                    } catch (Exception e) {
                        // log.warn
                    }
//                }, getSongDetailTaskExecutor);
            }
            System.out.println("-------------");
            for (String string : strings) {
                System.out.println(string);
            }
            System.out.println("-------------");
        } catch (Exception e) {
//            log.warn("fixedProjectStatusModify error {}", e.getMessage(), e);
        } finally {
//            if (lock) {
//                RedisLockUtils.releaseLock(redisTemplate, NftConstants.MODIFY_START_PROJECT_LOCK);
//            }
        }
    }

    private SongVo createSong(Element tr) {
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

        // int songMapper.insertSong(song); Enum 만들기
        // throw ServieException

        return song;
    }
}
