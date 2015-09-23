package org.david;

import java.util.Scanner;


/**
 * 1. 你首先说出三个不同的特殊数，要求必须是个位数，比如3、5、7。
2. 让所有学生拍成一队，然后按顺序报数。
3. 学生报数时，如果所报数字是第一个特殊数（3）的倍数，那么不能说该数字，而要说Fizz；如果所报数字是第二个特殊数（5）的倍数，那么要说Buzz；如果所报数字是第三个特殊数（7）的倍数，那么要说Whizz。
4. 学生报数时，如果所报数字同时是两个特殊数的倍数情况下，也要特殊处理，比如第一个特殊数和第二个特殊数的倍数，那么不能说该数字，而是要说FizzBuzz, 以此类推。如果同时是三个特殊数的倍数，那么要说FizzBuzzWhizz。
5. 学生报数时，如果所报数字包含了第一个特殊数，那么也不能说该数字，而是要说相应的单词，比如本例中第一个特殊数是3，那么要报13的同学应该说Fizz。如果数字中包含了第一个特殊数，那么忽略规则3和规则4，比如要报35的同学只报Fizz，不报BuzzWhizz。
 * @author david
 * 
 * http://www.cnblogs.com/iyangyuan/p/3710383.html  javascript版本的实现
 * 
 * 关键点：
 * 1 关于处理链的构成，作者采用了next句柄的自构成链。当然还可以采用List之类的将终止与控制单独统一处理，需要考虑两者的优劣，并实现另一版本
 * 2 就是关于几个数整除的算法，一开始想采用或或的关系法，但是这样第一没有扩展性同时表达式很痛苦，最终采用了这个相对较为直接的累加法
 *
 */

public class CountOff {

	abstract static class Handler {
		protected Handler next;
		protected int[] specialInts;
		protected static String[] words = { "Fizz", "Bizz", "Whizz" };

		Handler(Handler next, int[] specialInts) {
			this.next = next;
			this.specialInts = specialInts;
		}

		void handle(int i) {
			if (!handledSelf(i)) {
				chain(i);
			}
		}

		protected abstract boolean handledSelf(int i);

		private void chain(int i) {
			next.handle(i);
		}

		protected void print(String s) {
			System.out.println(s);
		}
		
		protected int divNum(int i)
		{
			int divExactlyNum = 0;
			for (int j = 0; j < specialInts.length; j++) {
				if (i % specialInts[j] == 0)
					divExactlyNum++;
			}
			return divExactlyNum;
		}
	}

	static class ContainingHandler extends Handler {
		private String specialIntStr;

		ContainingHandler(Handler next, int[] specialInts) {
			super(next, specialInts);
			this.specialIntStr = String.valueOf(specialInts[0]);
		}

		@Override
		protected boolean handledSelf(int i) {
			if (specialIntStr.contains(String.valueOf(i))) {
				print("Fizz");
				return true;
			}
			return false;

		}

	}

	static class MoreThan2TimesHandler extends Handler {

		MoreThan2TimesHandler(Handler next, int[] specialInts) {
			super(next, specialInts);
		}

		@Override
		protected boolean handledSelf(int i) {
			if (divNum(i) == 2 || divNum(i) == 3) {
				StringBuilder sb = new StringBuilder();
				if (i % specialInts[0] == 0)
					sb.append(words[0]);
				if (i % specialInts[1] == 0)
					sb.append(words[1]);
				if (i % specialInts[2] == 0)
					sb.append(words[2]);
				print(sb.toString());
				return true;
			}
			return false;
		}

	}

	static class TimesHandler extends Handler {

		TimesHandler(Handler next, int[] specialInts) {
			super(next, specialInts);
		}

		@Override
		protected boolean handledSelf(int i) {
			
			if (divNum(i) == 1) {
				if (i % specialInts[0] == 0) {
					print(words[0]);
					return true;
				}
				if (i % specialInts[1] == 0) {
					print(words[1]);
					return true;
				}
				if (i % specialInts[2] == 0) {
					print(words[2]);
					return true;
				}
			}
			return false;
		}

	}

	static class DefaultHandler extends Handler {
		DefaultHandler(Handler next, int[] specialInts) {
			super(next, specialInts);
		}

		@Override
		protected boolean handledSelf(int i) {
			print(String.valueOf(i));
			return true;
		}
	}

	public static void main(String[] arg) {
		Scanner s = new Scanner(System.in);
		int first = s.nextInt();
		int second = s.nextInt();
		int third = s.nextInt();
		int[] specialInts = { first, second, third };
		Handler defaultHandler = new DefaultHandler(null, specialInts);
		Handler timesHandler = new TimesHandler(defaultHandler, specialInts);
		Handler moreThan2TimesHandler = new MoreThan2TimesHandler(timesHandler,
				specialInts);
		Handler containingHandler = new ContainingHandler(
				moreThan2TimesHandler, specialInts);
		for (int i = 1; i < 101; i++) {
			containingHandler.handle(i);
		}

	}

}
