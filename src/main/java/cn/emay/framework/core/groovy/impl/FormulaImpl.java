package cn.emay.framework.core.groovy.impl;

import cn.emay.framework.core.groovy.IScript;
import org.springframework.stereotype.Component;

/**
 * 公式脚本
 */
@Component
public class FormulaImpl implements IScript {
	public Double add(Double a,Double b) {
		return a*b;
	}
}