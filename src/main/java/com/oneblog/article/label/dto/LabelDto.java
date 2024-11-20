package com.oneblog.article.label.dto;


import com.oneblog.article.label.LabelName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabelDto {

	private Long labelId;

	private LabelName name;

}
