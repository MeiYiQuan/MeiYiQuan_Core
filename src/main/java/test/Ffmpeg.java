package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

/**
 * 作者：齐潮
 * 创建日期：2017年1月19日
 * 类说明：
 */
public class Ffmpeg {

	public static void main(String[] args) throws IOException {
		
		/*
		String str = "ffmpeg -i C:\\Users\\Administrator\\Desktop\\美艺圈\\视频\\hehe.mp4 -r 10 -vframes 11 -s 1027x968 C:\\Users\\Administrator\\Desktop\\美艺圈\\视频\\image-ss%02d.jpeg";
//		StringTokenizer st = new StringTokenizer(str);
//		while(st.hasMoreTokens()){
//			System.out.println(st.nextToken());
//		}
		
		
		Runtime runtime = Runtime.getRuntime();
		Process p = runtime.exec(str);
		InputStream input = p.getInputStream();
		
		System.out.println(input.read());
		System.out.println(p.exitValue());
		p.destroy();
		*/
		
//		String value = "s12312";
//		boolean b = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,18}").matcher(value).find();
//		System.out.println(b);
		
//		　　String result = processFLV("E:\\test\\京视传媒\\体育类\\xiao.flv");
//		     
//		　　PatternCompiler compiler =new Perl5Compiler();
//		　　try {
//		　　String regexDuration ="Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
//		　　String regexVideo ="Video: (.*?), (.*?), (.*?)[,\\s]";
//		　　String regexAudio ="Audio: (\\w*), (\\d*) Hz";
//		     
//		　　Pattern patternDuration = compiler.compile(regexDuration,Perl5Compiler.CASE_INSENSITIVE_MASK);
//		　　PatternMatcher matcherDuration = new Perl5Matcher();
//		　　if(matcherDuration.contains(result, patternDuration)){
//		　　MatchResult re = matcherDuration.getMatch();
//		  
//		　　System.out.println("提取出播放时间  ===" +re.group(1));
//		　　System.out.println("开始时间        =====" +re.group(2));
//		　　System.out.println("bitrate 码率 单位 kb==" +re.group(3));
//		　　}
//		         
//		　　Pattern patternVideo = compiler.compile(regexVideo,Perl5Compiler.CASE_INSENSITIVE_MASK);
//		　　PatternMatcher matcherVideo = new Perl5Matcher();
//		         
//		　　if(matcherVideo.contains(result, patternVideo)){
//		　　MatchResult re = matcherVideo.getMatch();
//		　　System.out.println("编码格式  ===" +re.group(1));
//		　　System.out.println("视频格式 ===" +re.group(2));
//		　　System.out.println(" 分辨率  == =" +re.group(3));
//		　　}
//		         
//		　　Pattern patternAudio = compiler.compile(regexAudio,Perl5Compiler.CASE_INSENSITIVE_MASK);
//		　　PatternMatcher matcherAudio = new Perl5Matcher();
//		         
//		　　if(matcherAudio.contains(result, patternAudio)){
//		　　MatchResult re = matcherAudio.getMatch();
//		　　System.out.println("音频编码             ===" +re.group(1));
//		　　System.out.println("音频采样频率  ===" +re.group(2));
//		　　}
//		  
//		　　} catch (MalformedPatternException e) {
//		　　e.printStackTrace();
//		　　}
//		  
//		　　}
//		     
//		　　//  ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
//		　　private static String processFLV(String inputPath) {
//		　　/*
//		　　if (!checkfile(inputPath)){
//		　　_log.warn(inputPath+" is not file");
//		　　return false;
//		　　}
//		　　*/
//		　　List<String> commend=new java.util.ArrayList<String>();
//		         
//		　　//        commend.add("e:\\videoconver\\ffmpeg\\ffmpeg ");//可以设置环境变量从而省去这行
//		　　commend.add("ffmpeg");
//		　　commend.add("-i");
//		　　commend.add(inputPath);
//		        
//		　　try {
//		  
//		　　ProcessBuilder builder = new ProcessBuilder();
//		　　builder.command(commend);
//		　　builder.redirectErrorStream(true);
//		　　Process p= builder.start();
//		  
//		　　//1. start
//		　　BufferedReader buf = null; // 保存ffmpeg的输出结果流
//		　　String line = null;
//		　　//read the standard output
//		  
//		　　buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		             
//		　　StringBuffer sb= new StringBuffer();
//		　　while ((line = buf.readLine()) != null) {
//		　　System.out.println(line);
//		　　sb.append(line);
//		　　continue;
//		　　}
//		　　int ret = p.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
//		　　//1. end
//		　　return sb.toString();
//		　　} catch (Exception e) {
//		　　//            System.out.println(e);
//		　　return null;
//		　　}
		
	}
}
