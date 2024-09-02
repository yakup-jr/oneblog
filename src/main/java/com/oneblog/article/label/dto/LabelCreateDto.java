package com.oneblog.article.label.dto;

import com.oneblog.article.label.LabelName;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Setter
@Getter
public class LabelCreateDto {

	private LabelName name;

}
