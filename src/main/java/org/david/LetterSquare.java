package org.david;

import java.util.ArrayList;
import java.util.List;

public class LetterSquare {

	private static class Element {
		private int x;
		private int y;

		Element(int x, int y) {
			this.x = x;
			this.y = y;
		}

		boolean isNeighboring(Element other) {
			if (((this.x == other.x) && (Math.abs(this.y - other.y) == 1))
					|| ((this.y == other.y) && (Math.abs(this.x - other.x) == 1)))
				return true;
			else
				return false;
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof Element))
				return false;
			Element other = (Element) obj;
			return (this.x == other.x) && (this.y == other.y);

		}
	}

	private List<Element> getOccursOf(char[][] board, char c) {
		List<Element> occurs = new ArrayList<Element>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == c) {
					occurs.add(new Element(i, j));
				}

			}
		}
		return occurs;
	}

	public boolean recursive(Element e, int nextIdx,
			List<List<Element>> allOccurs, Element[] routeElements) {

		int currentIdx = nextIdx - 1;

		for (int i = currentIdx; i < allOccurs.size(); i++)
			routeElements[currentIdx] = null;
		if (nextIdx > 0) {
			for (int i = 0; i < currentIdx; i++) {
				if (e.equals(routeElements[i]))
					return false;
			}
		}
		if (nextIdx == allOccurs.size()) {
			routeElements[currentIdx] = e;
			return true;
		}

		List<Element> neighboringElements = allOccurs.get(nextIdx);
		for (Element neighboringElement : neighboringElements) {
			boolean isNeighboring = e.isNeighboring(neighboringElement);

			if (isNeighboring) {
				routeElements[currentIdx] = e;
				boolean isRecursiveNeighboring = recursive(neighboringElement,
						nextIdx + 1, allOccurs, routeElements);
				if (isRecursiveNeighboring) {
					return true;
				}
			}

		}
		return false;

	}

	public boolean exist(char[][] board, String word) {
		List<List<Element>> allOccurs = new ArrayList<List<Element>>();
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			List<Element> cOccurs = getOccursOf(board, c);
			allOccurs.add(cOccurs);
		}
		List<Element> firtCharOccurs = allOccurs.get(0);
		for (Element occur : firtCharOccurs) {
			Element[] routeElements = new Element[word.length()];
			if (recursive(occur, 1, allOccurs, routeElements)) {
				return true;
			}
		}
		return false;

	}

	public static void main(String[] arg) {
		LetterSquare s = new LetterSquare();
		char[][] board = { { 'A', 'B', 'C', 'E' }, { 'S', 'F', 'C', 'S' },
				{ 'A', 'D', 'E', 'E' } };
		System.out.println(s.exist(board, "ABCCED"));
		System.out.println(s.exist(board, "SEE"));
		System.out.println(s.exist(board, "ABCB"));

	}
}
