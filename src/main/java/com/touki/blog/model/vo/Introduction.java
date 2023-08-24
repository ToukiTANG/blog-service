package com.touki.blog.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Touki
 */
@Data
public class Introduction {

    private String avatar;

    private String nickname;

    private String description;

    private String github;

    private String bilibili;

    private String netease;

    private List<Favorite> favorites;
}
