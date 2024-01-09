package eca.learnings.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.HashMap;

public class CountCharacters {

	public static void main(String[] args) {
		SpringApplication.run(CountCharacters.class, args);
	}

	int ans = countDuplicateChars("Java");

	public int countDuplicateChars(String inputString){
		HashMap<Character, Integer> charMap = new HashMap<>();
		int count = 0;
		for(int i = 0; i<inputString.length(); i++){
			if(!charMap.containsKey(inputString.charAt(i)))
			{
				charMap.put(inputString.charAt(i), 1);
			}
			else count++;

		}
		System.out.println("Duplicate chars count: "+count);
		return count;

	}
}
