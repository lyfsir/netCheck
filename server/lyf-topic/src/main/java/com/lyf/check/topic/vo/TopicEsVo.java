package com.lyf.check.topic.vo;

/**
 * @author lyf
 * @date 2020/8/24-5:12
 */

import lombok.Data;

import java.util.List;

/**
 * {
 *   "mappings": {
 *     "properties": {
 *       "id": {
 *         "type": "long"
 *       },
 *       "content": {
 *         "type": "text",
 *         "analyzer": "ik_smart"
 *       },
 *       "username": {
 *         "type": "keyword"
 *       },
 *       "logo": {
 *         "type": "keyword",
 *         "index": false,
 *         "doc_values": false
 *       },
 *       "createTime": {
 *         "type": "keyword",
 *         "index": false,
 *         "doc_values": false
 *       },
 *       "image":{
 *         "type": "nested",
 *         "properties": {
 *           "imageurl":{
 *             "type": "keyword",
 *             "index": false,
 *             "doc_values": false
 *           }
 *         }
 *       }
 *     }
 *   }
 * }
 */
@Data
public class TopicEsVo {
    private Long id;
    private String content;
    private String userName;
    private Long uId;
    private String header;
    private String createTime;
    private List<Image> image;

    @Data
    public static class Image{
        private String imageurl;
    }
}
