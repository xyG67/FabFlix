package cs122B.FabFlix.Model;

import java.util.Hashtable;

public class ShoppingChart {
		private Hashtable<Movies, Integer> shoppingChart = new Hashtable<Movies, Integer>();
		
		public ShoppingChart(Movies m){
			//this.shoppingChart.put
		}
		public void addMovieKey(Movies m) {
			this.shoppingChart.put(m, 1);
		}
		
		public void addOneMovie(Movies m) {
			int i = this.shoppingChart.get(m) + 1;
			this.shoppingChart.put(m, i);
		}
		
		public void removeOneMovie(Movies m) {
			int i = this.shoppingChart.get(m) - 1;
			this.shoppingChart.put(m, i);
		}
		
		public void removeMovieKey(Movies m) {
			this.shoppingChart.remove(m);
		}
		
		public boolean isContain(Movies m) {
			return this.shoppingChart.containsKey(m);
		}
		
		public boolean isEmpty() {
			return this.shoppingChart.isEmpty();
		}
}
