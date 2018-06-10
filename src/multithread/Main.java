package multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws IOException {
		Main program = new Main();
		program.start();
	}

	//計算する配列の生成
	private void start() throws IOException {
		//計算する配列の生成
		int arraySize = 100000000;
		int[] numList = new int[arraySize];
		Random random = new Random();
		for(int i = 0; i < numList.length; i++) {
			int num = random.nextInt(10);
			numList[i] = num;
		}

		while(true) {
			//計算スレッドの数の入力
			System.out.println("スレッド数を" + arraySize + "の約数で入力してください");
			System.out.println("終了したい場合はそれ以外を入力してください");
			String numThreadStr = input();
			try {
				int numThread = Integer.parseInt(numThreadStr);
				//マルチスレッド処理呼び出し
				calcSumMulti(numList, numThread);
			}
			catch(NumberFormatException e) {
				break;
			}
		}
	}

	private void calcSumMulti(int[] numList,int numThread) {
		//計算リストの分割とスレッド生成
		List<MultiThread> threadList = new ArrayList<MultiThread>();
		int singleListSize = numList.length / numThread;
		for(int i = 0; i < numThread; i++) {
			int first = i * singleListSize;
			int last = first + singleListSize;
			int[] singleNumList = Arrays.copyOfRange(numList, first, last);
			MultiThread mt = new MultiThread(singleNumList);
			threadList.add(mt);
		}

		//計算時間測定開始
		long startTime = System.currentTimeMillis();

		//計算開始
		for(Thread thread : threadList) {
			thread.start();
		}

		//計算結果集計
		int sum = 0;
		for(MultiThread thread : threadList) {
			try {
				thread.join();
				sum += thread.getSum();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//計算結果の表示
		System.out.println("計算結果は" + sum + "でした");
		long calcTime = System.currentTimeMillis() - startTime;
		System.out.println("計算時間は" + calcTime + "msでした");
	}

	public String input() throws IOException {
		//コンソール入力
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		return br.readLine();
	}
}

class MultiThread extends Thread {
	private int sum;
	private int[] numList;

	public MultiThread(int[] numList) {
		this.sum = 0;
		this.numList = numList;
	}

	public void run() {
		for(int num : this.numList) {
			this.sum += num;
		}
	}

	public int getSum() {
		return this.sum;
	}
}