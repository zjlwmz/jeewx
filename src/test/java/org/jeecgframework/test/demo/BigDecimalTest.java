package org.jeecgframework.test.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BigDecimalTest {

	public static void main(String[] args) {
		
		BigDecimal d1=new BigDecimal(1);
		BigDecimal d2=new BigDecimal(1.001);
//		BigDecimal d3=d1.divide(d2, 2);
		BigDecimal d3=d1.divide(d2, 3, BigDecimal.ROUND_HALF_EVEN);
		System.out.println(d3+"--"+d3.intValue());
		
		
		DecimalFormat df = new DecimalFormat("#.00");
//		df.setGroupingSize(0);/
		df.setRoundingMode(RoundingMode.FLOOR);
		System.out.println(df.format(0.999));
		
		System.out.println(String.format("%.2f",0.999)+"--");
	}
}
