package com.knoldus.entity;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

    private static List<Map<String, String>> song = Arrays.asList(
            ImmutableMap.of("id", "song-1",
                    "name", "Shape of you",
                    "genre", "Pop",
                    "artist", "artist-1"),
            ImmutableMap.of("id", "song-2",
                    "name", "Closer",
                    "genre", "Electronic/Dance",
                    "artist", "artist-2"),
            ImmutableMap.of("id", "song-3",
                    "name", "Señorita",
                    "genre", "Pop",
                    "artist", "artist-3")
    );

    private static List<Map<String, String>> artist = Arrays.asList(
            ImmutableMap.of("id", "artist-1",
                    "firstName", "Ed",
                    "lastName", "Sheeran"),
            ImmutableMap.of("id", "artist-2",
                    "firstName", "ChainSmokers",
                    "lastName", ""),
            ImmutableMap.of("id", "artist-3",
                    "firstName", "Shawn/Camila",
                    "lastName", "Mendes/Cabello")
    );

    public DataFetcher getSongByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String songID = dataFetchingEnvironment.getArgument("id");
            return song
                    .stream()
                    .filter(book -> book.get("id").equals(songID))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getArtistByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> song = dataFetchingEnvironment.getSource();
            String artistID = song.get("artistID");
            return artist
                    .stream()
                    .filter(payment -> payment.get("id").equals(artistID))
                    .findFirst()
                    .orElse(null);
        };
    }
}