package com.intel.analytics.zoo.pipeline.inference;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;
import java.nio.file.*;



/**
 * Created by xiaxue on 7/13/18.
 */
public class InferenceTest extends Thread{
	TestClassificationModel model = new TestClassificationModel(1);
	GloveTextProcessing textPreprocessor = new GloveTextProcessing();
	String glovepath = "/home/xiaxue/data/text-classification/glove.6B/glove.6B.200d.txt";
	Map<String, List<Float>> embmap = textPreprocessor.loadEmbedding(glovepath);

	public void run(){
		JTensor tensorinput = new JTensor();
		int[] shape = new int[]{500, 200};
		tensorinput.setShape(shape);
		ArrayList<Float> data = new ArrayList<Float>();
		String filePath = "/home/xiaxue/data/text-classification/20news-18828/sci.med/59599";//sci.med #14


		ArrayList<JTensor> inputlist = new ArrayList<JTensor>();
		try{
			String text = new String (Files.readAllBytes( Paths.get(filePath) ) );
			JTensor tensor = textPreprocessor.preprocessWithEmbMap(text, 10, 500, embmap);
			inputlist.add(tensor);
		}catch(IOException e){
			e.printStackTrace();
		}


//		Random r = new Random();
//		for (int i = 0; i < 100000; i++){
//			float vector = -1 + r.nextFloat()*2;
//			data.add(vector);
//		}
//		tensorinput.setData(data);
//		ArrayList<JTensor> inputlist = new ArrayList<JTensor>();
//		for (int i = 0; i < 500; i++){
//			inputlist.add(tensorinput);
//		}


//		try{
////			model.predict(inputlist);
//			System.out.println(model.predict(inputlist).toString());
//		}catch (InterruptedException e){
//			e.printStackTrace();
//		}
		System.out.println(model.predict(inputlist).toString());
	}
	public static void main(String args[]){
		long begin = System.currentTimeMillis();
		InferenceTest test = new InferenceTest();
		test.model.load("/home/xiaxue/data/text-classification/textClassificationModel");

		Thread[] threadArray = new Thread[8];
		for (int i = 0; i < 8; i++){
			Thread thread = new Thread(test);
			threadArray[i] = thread;
			threadArray[i].start();
		}

		try {
			for (int i = 0; i < 8; i++){
				threadArray[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("#####time elapse " + (end - begin));

	}

}
