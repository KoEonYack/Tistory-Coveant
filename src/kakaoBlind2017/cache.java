package kakaoBlind2017;

public class cache {
	public int solution (int cacheSize, String[] cities) {
		int answer = 0;
		String [] cache = new String [cacheSize];
		String cityName;
		
		if (cacheSize == 0) {
			return cities.length * 5;
		}
		
		for(int i =0; i<cities.length; i++) {  // 배열로 들어온 모든 입력에 대해서 처리해 주어야 한다. 
			cityName = cities[i].toLowerCase();
			for(int j=0; j < cacheSize; j++) { // 캐시에 해당 도시가 저장되어 있는지 확인해야한다. 
				if (cityName.equals(cities[j]) ) {
					
				}
			}
		}
		
		
		return answer;
	}
	
	public static void main(String[] args) {
		System.out.println("Hello");
		

	}
}
