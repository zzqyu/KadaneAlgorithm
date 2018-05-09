/*
숫자 배열 정사각형 내에 최대/최소 합 구하기

문제를 풀기 위해 찾아본 영상 : https://www.youtube.com/watch?v=yCQN096CwWM

프로그래밍 언어 : JAVA
작업IDE :  이클립스
컴파일러 : javac 1.8.0_144
운영체제 : WINDOWS 10
테스트 컴퓨터 사양 : i7-6500U 2.5Ghz 8GB RAM
테스트 시 소요시간 (1000개 기준) : 2.1~2.3 sec
*/

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KadaneAlgorithm {
	static int count;
	public static void main (String[] args) 
    {
		//long time = System.currentTimeMillis ( );
		Scanner sc = new Scanner(System.in); //input
		count = sc.nextInt(); //정사각형 사이즈
		int[][] list = new int[count][]; //정사각형 배열 Count X Count
		for(int i = 0; i< count; i++) { //배열 채우기
			list[i] = new int[count];
			for(int j=0; j<count; j++) list[i][j] = sc.nextInt();
		}
		sc.close(); //다 채우면 Input 종료
	
		maxMinSum(list); //최대 최소 합 구하기
        //System.out.println((System.currentTimeMillis ( ) - time)+"ms");
    }
	
	public static int[][] curMaxMin(int[] a) {
		int[] minAnswer = new int[] {Integer.MAX_VALUE, 0, -1}; //최소값 결과, 시작 x좌표, 끝 x좌표
		int[] maxAnswer = new int[] {Integer.MIN_VALUE, 0, -1}; //최대값 결과, 시작 x좌표, 끝 x좌표
		int minCurSum = 0; //현재 최소값
		int maxCurSum = 0; //현재 최대값
		int minX1 = 0; //최소일 경우 시작 X좌표
		int maxX1 = 0; //최대일 경우 시작 X좌표
		//정사각형 이차원 배열의 한 줄 크기 만큼 도는 루프
		for (int i = 0; i < count; i++) {
			//최소값
			//더한 값이 양수일 경우 최소값이 될 확률이 적으므로 0으로 초기화
			minCurSum = ((minCurSum+=a[i]) > 0 ) ? 0: minCurSum;
			if(minCurSum == 0) minX1 = i+1; //시작 좌표를 1더한다.
			//더한 값이 최소값보다 작은 경우 값을 갱신해 준다.
			else if(minCurSum < minAnswer[0]) {
				minAnswer[0] = minCurSum;
				minAnswer[1] = minX1;
				minAnswer[2] = i;
			}
			//최대값
			//더한 값이 음수일 경우 최대값이 될 확률이 적으므로 0으로 초기화
			maxCurSum = ((maxCurSum+=a[i]) < 0 ) ? 0: maxCurSum;
			if(maxCurSum == 0) maxX1 = i+1; //시작 좌표를 1더한다.
			//더한 값이 최대값보다 큰 경우 값을 갱신해 준다.
			else if(maxCurSum > maxAnswer[0]) {
				maxAnswer[0] = maxCurSum;
				maxAnswer[1] = maxX1;
				maxAnswer[2] = i;
			}
		}
		//최소값
		//배열의 모든 값이 양수인 경우 위에서 구해진 값이 없다. 제일 작은 양수가 최소합이 됨
		if(minAnswer[2] == -1) {
			//배열의 최소값을 구한다. 
			minAnswer[0] = Collections.min(IntStream.of( a ).boxed().collect( Collectors.toList() ));
			//최소값을 가진 배열의 위치를 구한다.
			minAnswer[1] = minAnswer[2] = Arrays.binarySearch(a, minAnswer[0]);
		}
		//최대값
		//배열의 모든 값이 음수인 경우 위해서 구해진 값이 없다. 제일 큰 음수가 최대합이 된다.
		if(maxAnswer[2] == -1) {
			//배열의 최대값을 구한다. 
			maxAnswer[0] = Collections.max(IntStream.of( a ).boxed().collect( Collectors.toList() ));
			//최댓값을 가진 배열의 위치를 구한다.
			minAnswer[1] = minAnswer[2] = Arrays.binarySearch(a, minAnswer[0]);
		}
		return new int[][] {minAnswer, maxAnswer};
	}
	
     
    public static void maxMinSum(int[][] items) {
        int[][] curResult;			//가로 세로 크기
        int max = Integer.MIN_VALUE;//최대 값
        int min = Integer.MAX_VALUE;//최소 값
        int minX1 = 0;				//최소 값일 경우 (x1,y1)(x2,y2)
        int minY1 = 0;
        int minX2 = 0;
        int minY2 = 0;
        int maxX1 = 0;				//최대 값일 경우 (x1,y1)(x2,y2)
        int maxY1 = 0;
        int maxX2 = 0;
        int maxY2 = 0;
		//합을 시작하는 row 번호 정하는 루프
        for(int startRow = 0; startRow < count; startRow++) {
        	int[] innerSum = new int[count]; //각 줄의 합을 저장할 배열 ([1,2][3,4]->[4,6])
        	for(int curRow = startRow; curRow < count; curRow++) { //시작 줄부터 마지막 줄까지 도는 루프
        		if(curRow == startRow) innerSum = items[startRow].clone(); //시작 시에는 해당 줄의 항목을 복사
        		else // 나머지 줄의 경우는 새로 넣는 항목을 기존 값에 더한다. 
					for (int i = 0; i < count; i++)
						innerSum[i] += items[curRow][i]; 
        		//위에서 만든 배열의 최대/최소 합 구하기
        		curResult = curMaxMin(innerSum);
        		//최대/최소 값 갱신
        		if (curResult[0][0] < min) {
        			min = curResult[0][0];
        			minX1 = curResult[0][1];
					minY1 = startRow;
					minX2 = curResult[0][2];
					minY2 = curRow;
				}
        		if (curResult[1][0] > max) {
        			max = curResult[1][0];
					maxX1 = curResult[1][1];
					maxY1 = startRow;
					maxX2 = curResult[1][2];
					maxY2 = curRow;
				}
        	}
        	
        }
		//결과
        System.out.println(max + "\n(" + maxX1 + ", " + maxY1 + ")(" + maxX2 + ", " + maxY2 + ")");
        System.out.println(min + "\n(" + minX1 + ", " + minY1 + ")(" + minX2 + ", " + minY2 + ")");
	}
   
}
