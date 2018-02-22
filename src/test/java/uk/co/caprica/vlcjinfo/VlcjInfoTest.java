/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package uk.co.caprica.vlcjinfo;


import java.io.OutputStreamWriter;

public class VlcjInfoTest {

    public static void main(String[] args) throws Exception {
    	System.load("C:/Program Files/MediaInfo/MediaInfo.dll");
    	
        String file = "C:\\Users\\nbhushan\\Documents\\testData\\DreamLooks.MP4";
        MediaInfo mediaInfo = MediaInfo.mediaInfo(file);
        
        mediaInfo.dump(new OutputStreamWriter(System.out));

        Section video = mediaInfo.first("Video");
        Integer width = video.integer("Width");
        Integer height = video.integer("Height");

        System.out.printf("%d x %d%n", width, height);
        System.out.printf("ID %d%n", video.integer("ID"));
        System.out.printf("Duration %s%n", video.duration("Duration"));
        System.out.printf("Frame rate %f%n", video.decimal("Frame rate"));

        Section audio = mediaInfo.first("Audio");
        System.out.printf("Duration Last Frame %s%n", audio.duration("Duration_LastFrame"));
    	
    }
}
