package com.intel.analytics.zoo.pipeline.inference;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by xiaxue on 7/13/18.
 */
public class InferenceTest extends Thread{
	TestClassificationModel model = new TestClassificationModel(2);

	public void run(){
		JTensor tensorinput = new JTensor();
		ArrayList<Integer> shape = new ArrayList<Integer>();
		shape.add(500);
		shape.add(200);
		tensorinput.setShape(shape);
		ArrayList<Float> data = new ArrayList<Float>();
		Random r = new Random();
		for (int i = 0; i < 100000; i++){
			float vector = -1 + r.nextFloat()*2;
			data.add(vector);
		}
		tensorinput.setData(data);
		ArrayList<JTensor> inputlist = new ArrayList<JTensor>();
		for (int i = 0; i < 500; i++){
			inputlist.add(tensorinput);
		}

		try{
//			model.predict(inputlist);
			System.out.println(model.predict(inputlist).toString());
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		long begin = System.currentTimeMillis();
		InferenceTest test = new InferenceTest();
		test.model.load("/home/xiaxue/data/text-classification/textClassificationModel");

		Thread[] threadArray = new Thread[20];
		for (int i = 0; i < 20; i++){
			Thread thread = new Thread(test);
			threadArray[i] = thread;
			threadArray[i].start();
		}

		try {
			for (int i = 0; i < 20; i++){
				threadArray[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("#####time elapse " + (end - begin));

	}

}
