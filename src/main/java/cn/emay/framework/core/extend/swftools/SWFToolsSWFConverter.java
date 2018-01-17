package cn.emay.framework.core.extend.swftools;

import java.io.File;
import java.io.IOException;

import cn.emay.framework.common.utils.FileUtils;
import cn.emay.framework.common.utils.LogUtil;
import cn.emay.framework.common.utils.PinyinUtil;

public class SWFToolsSWFConverter implements SWFConverter {
	
	/** SWFTools pdf2swf.exe路径 */
//	private static String PDF2SWF_PATH = ConStant.SWFTOOLS_PDF2SWF_PATH;

	public void convert2SWF(String inputFile, String swfFile, String extend) {
		File pdfFile = new File(inputFile);
		File outFile = new File(swfFile);

		if (!pdfFile.exists()) {
			LogUtil.info("PDF文件不存在！");
			return;
		}

		if (outFile.exists()) {
			LogUtil.info("SWF文件已存在！");
			return;
		}
		String command = ConStant.getSWFToolsPath(extend) + " \"" + inputFile

		+ "\" -o " + " \"" + swfFile + " \"" + " -s languagedir=D:\\xpdf-chinese-simplified -T 9 -f";
		// + "\" -o " + swfFile +
		// " -s languagedir=D:\\xpdf-chinese-simplified -T 9 -f";

		try {
			// 开始转换文档
			Process process = Runtime.getRuntime().exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");
			errorGobbler.start();
			outputGobbler.start();
			try {
				process.waitFor();
				LogUtil.info("时间-------" + process.waitFor());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convert2SWF(String inputFile, String extend) {
		String swfFile = PinyinUtil.getPinYinHeadChar(FileUtils.getFilePrefix2(inputFile)) + ".swf";
		convert2SWF(inputFile, swfFile, extend);
	}
}
