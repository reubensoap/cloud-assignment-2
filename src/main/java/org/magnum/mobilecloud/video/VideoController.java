/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;


import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class VideoController {

	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it to
	 * something other than "AnEmptyController"
	 * 
	 * 
	 * ________ ________ ________ ________ ___ ___ ___ ________ ___ __ |\ ____\|\ __
	 * \|\ __ \|\ ___ \ |\ \ |\ \|\ \|\ ____\|\ \|\ \ \ \ \___|\ \ \|\ \ \ \|\ \ \
	 * \_|\ \ \ \ \ \ \ \\\ \ \ \___|\ \ \/ /|_ \ \ \ __\ \ \\\ \ \ \\\ \ \ \ \\ \ \
	 * \ \ \ \ \\\ \ \ \ \ \ ___ \ \ \ \|\ \ \ \\\ \ \ \\\ \ \ \_\\ \ \ \ \____\ \
	 * \\\ \ \ \____\ \ \\ \ \ \ \_______\ \_______\ \_______\ \_______\ \ \_______\
	 * \_______\ \_______\ \__\\ \__\ \|_______|\|_______|\|_______|\|_______|
	 * \|_______|\|_______|\|_______|\|__| \|__|
	 * 
	 * 
	 */
	
	@Autowired
	private VideoRepository videoRepository;

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		return Lists.newArrayList(videoRepository.findAll());
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}",  method = RequestMethod.GET)
	public @ResponseBody Video getVideoById(@PathVariable(value = "id") long id) {
		Video video = videoRepository.findOne(id);
		if (video != null) {
			return video;
		} else {
			throw new ResourceNotFoundException();
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findByTitle(@RequestParam(value = VideoSvcApi.TITLE_PARAMETER) String title) {
		return videoRepository.findByName(title);
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findByDurationLessThan(@RequestParam(value = VideoSvcApi.DURATION_PARAMETER) long duration,
																  Principal p) {
		return videoRepository.findByDurationLessThan(duration);
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video video,
										Principal p) {
		return videoRepository.save(video);
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
	public ResponseEntity<Void> likeVideo(@PathVariable(value = "id") long id,
									Principal p) {
		Video video = videoRepository.findOne(id);
		if (video == null) {
			throw new ResourceNotFoundException();
		}
		if (video.alreadyLiked(p.getName())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			video.like(p.getName());
			videoRepository.save(video);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
	public ResponseEntity<Void> unlikeVideo(@PathVariable(value = "id") long id,
						  Principal p) {
		Video video = videoRepository.findOne(id);
		if (video == null) {
			throw new ResourceNotFoundException();
		}
		if (video.alreadyLiked(p.getName())) {
			video.unlike(p.getName());
			videoRepository.save(video);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}