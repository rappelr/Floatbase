package com.rappelr.floatbase.skin;

import com.rappelr.floatbase.utils.Identifiable;
import com.rappelr.floatbase.utils.StringUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SkinInstance extends Identifiable {

	@Getter
	private final Skin skin;

	@Getter
	private final Condition condition;

	@Getter
	private final Attribute attribute;

	public boolean match(Skin skin, Condition condition, Attribute attribute) {
		return skin.equals(this.skin) && condition == this.condition && attribute == this.attribute;
	}

	public boolean match(SkinInstance instance) {
		return instance.getSkin().equals(this.skin) && instance.condition == this.condition && instance.attribute == this.attribute;
	}

	public String getFormal() {
		return SkinInstance.getFormal(skin, condition, attribute);
	}
	
	public static String getFormal(Skin skin, Condition condition, Attribute attribute) {
		val prefix = attribute.getFormal();
		return (prefix.isEmpty() ? "" : prefix + " ")
				+ skin.getFormal() + " "
				+ StringUtil.lignParentheses(condition.getFormal());
	}

	@Override
	protected String generateId() {
		return StringUtil.flatten(attribute.getSimple()) + "_" + StringUtil.flatten(condition.getSimple()) + "_" + skin.getId();
	}

}
