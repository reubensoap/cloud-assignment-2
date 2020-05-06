package org.magnum.mobilecloud.video;


import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource(path = VideoSvcApi.VIDEO_SVC_PATH)
interface VideoRepository extends JpaRepository<Video, Long> {
    Video findById(long id);

    Collection<Video> findByName(@Param(VideoSvcApi.TITLE_PARAMETER) String name);

    Collection<Video> findByDurationLessThan(@Param(VideoSvcApi.DURATION_PARAMETER) long duration);
}