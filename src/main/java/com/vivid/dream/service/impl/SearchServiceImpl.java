package com.vivid.dream.service.impl;

import com.vivid.dream.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
//    private final RestClient restClient;
//    private final ElasticsearchClient esClient;
//    private static final int HIGHLIGHT_FRAGMENT_SIZE = 50;
//
//    @Override
//    public String getLastIndexName(String name) throws IOException {
//        String indexName = "";
//        Request request = new Request("GET", "_cat/indices/"+name+"-*?s=index:desc&h=index,health");
//        Response response = restClient.performRequest(request);
//
//        InputStream inputStream = response.getEntity().getContent();
//        List<String> indexes = new BufferedReader(new InputStreamReader(inputStream))
//                .lines()
//                .collect(Collectors.toList());
//
//        if (indexes.size() > 1) {
//            String[] nowIndex = indexes.get(0).split(" ");
//            String[] oldIndex = indexes.get(1).split(" ");
//            indexName = "green".equals(nowIndex[1]) || "yellow".equals(nowIndex[1]) ? nowIndex[0] : oldIndex[0];
//        }
//        return indexName;
//    }
//
//    @Override
//    public List<ResponseSongInfoSearch> getSongInfoSearch(String keyword) throws IOException {
//        String indexName = getLastIndexName("song_info");
//        Highlight highlight = getHighlightQuery();
//        Query query = getQuery(keyword);
//
//        SearchRequest searchRequest = new SearchRequest.Builder()
//                .index(indexName)
//                .query(query)
//                .highlight(highlight)
//                .build();
//
//        SearchResponse<HashMap> searchResponse = esClient.search(searchRequest, HashMap.class);
//        return getResponseSongInfoSearch(searchResponse);
//    }
//
//    @Override
//    public List<ResponsePopularSearchWord> getPopularSearchWords(int beforeDays, int limit) throws Exception {
//        String gte = LocalDateTime.now().minusDays(beforeDays).toString();
//        RangeQuery dateRange = getRangeQuery("received_at", gte, LocalDateTime.now().toString());
//
//        SearchRequest searchRequest = new SearchRequest.Builder()
//                .index("nginx-*")
//                .query(q -> q.range(dateRange))
//                .size(0)
//                .aggregations("params_searchWord",
//                    a -> a.terms(
//                        t -> t.field("params.searchWord.keyword")
//                        .size(limit)))
//                .build();
//
//        SearchResponse<HashMap> searchResponse = esClient.search(searchRequest, HashMap.class);
//        AggregateVariant agg = searchResponse.aggregations().get("params_searchWord")._get();
//        List<StringTermsBucket> buckets = ((StringTermsAggregate) agg).buckets().array();
//
//        List<ResponsePopularSearchWord> populars = new ArrayList<>();
//        for (StringTermsBucket bucket : buckets) {
//            ResponsePopularSearchWord responsePopularSearchWord = ResponsePopularSearchWord.builder()
//                    .docCount(bucket.docCount())
//                    .key(bucket.key()).build();
//
//            populars.add(responsePopularSearchWord);
//        }
//        return populars;
//    }
//
//    private List<ResponseSongInfoSearch> getResponseSongInfoSearch(SearchResponse<HashMap> searchResponse) {
//        List<ResponseSongInfoSearch> results = new ArrayList<>();
//        List<Hit<HashMap>> hits = searchResponse.hits().hits();
//        for (Hit<HashMap> hit : hits) {
//            results.add(getResponseSongInfoSearch(hit));
//        }
//
//        return results;
//    }
//
//    private ResponseSongInfoSearch getResponseSongInfoSearch(Hit<HashMap> hit) {
//        HashMap source = hit.source();
//
//        String songName = source.get("song_name").toString();
//        String artist = source.get("artist").toString();
//        String artistByHighlight = "";
//        String songNameByHighlight = "";
//        String lyricsByHighlight = "";
//
//        Map<String, List<String>> hitHighlight = hit.highlight();
//        for (String key : hitHighlight.keySet()) {
//            switch (key){
//                case "artist":
//                    artistByHighlight = hitHighlight.get(key).toString(); break;
//                case "song_name" :
//                    songNameByHighlight = hitHighlight.get(key).toString(); break;
//                case "lyrics" :
//                    lyricsByHighlight = hitHighlight.get(key).toString(); break;
//                default: break;
//            }
//        }
//
//        ResponseSongInfoSearch searchSongInfo = ResponseSongInfoSearch.builder()
//                .songName(defaultString(songName, ""))
//                .artist(defaultString(artist, ""))
//                .artistByHighlight(artistByHighlight)
//                .songNameByHighlight(songNameByHighlight)
//                .lyricsByHighlight(lyricsByHighlight)
//                .build();
//        return searchSongInfo;
//    }
//
//    private Query getQuery(String keyword) {
//        List<Query> queries = getQueries(keyword);
//        Query query = Query.of(q -> q.bool(b -> b.should(queries)));
//        return query;
//    }
//
//    private List<Query> getQueries(String keyword) {
//        List<Query> queries = new ArrayList<>();
//        Query artistQuery = Query.of(
//            q -> q.match(MatchQuery.of(m -> m.field("artist")
//                .query(keyword)
//                .boost(3.0F)))
//        );
//
//        Query songNameQuery = Query.of(
//            q -> q.match(MatchQuery.of(m -> m.field("song_name")
//                .query(keyword)
//                .boost(2.0F)))
//        );
//
//        Query lyricsQuery = Query.of(
//            q -> q.match(MatchQuery.of(m -> m.field("lyrics")
//                .query(keyword)
//                .boost(1.0F)))
//        );
//
//        queries.add(artistQuery);
//        queries.add(songNameQuery);
//        queries.add(lyricsQuery);
//        return queries;
//    }
//
//    private Highlight getHighlightQuery() {
//        HashMap<String, HighlightField> hashMap = new HashMap<>();
//        hashMap.put("artist", new HighlightField.Builder().build());
//        hashMap.put("song_name", new HighlightField.Builder().build());
//        hashMap.put("lyrics", new HighlightField.Builder().build());
//
//        Map<String, HighlightField> map = new HashMap<>();
//        map.put("lyrics", HighlightField.of(hf -> hf.numberOfFragments(20)));
//        map.put("song_name", HighlightField.of(hf -> hf.numberOfFragments(0)));
//        map.put("artist", HighlightField.of(hf -> hf.numberOfFragments(0)));
//
//        Highlight highlight = Highlight.of(h -> h
//                .fragmentSize(HIGHLIGHT_FRAGMENT_SIZE)
//                .preTags("<b>")
//                .postTags("</b>")
//                .fields(map)
//                .fields( hashMap )
//        );
//        return highlight;
//    }
//
//    private RangeQuery getRangeQuery(String fieldRange, String gte, String lte) {
//        RangeQuery dateRange = RangeQuery.of(r -> r
//                .field(fieldRange)
//                .gte(JsonData.of(gte))
//                .lte(JsonData.of(lte))
//        );
//        return dateRange;
//    }
}