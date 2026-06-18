package com.akshay.phase05_tdd_coverage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PRACTICE SESSION 1 — TDD: StringCalculator
 * Phase 5: Red → Green → Refactor
 *
 * TDD Steps followed:
 *
 * STEP 1 — RED:    test add("") == 0         → failed (class didn't exist)
 *          GREEN:  returned hardcoded 0
 *
 * STEP 2 — RED:    test add("1") == 1         → failed (returned 0 always)
 *          GREEN:  added isEmpty() + parseInt
 *
 * STEP 3 — RED:    test add("1,2") == 3       → failed (parseInt crashed on comma)
 *          GREEN:  added split(",") + loop sum
 *
 * STEP 4 — RED:    test add("1\n2,3") == 6    → failed (newline not a delimiter)
 *          GREEN:  changed split to [,\n]
 *
 * STEP 5 — RED:    test add("-1,2") throws    → failed (no validation)
 *          GREEN:  added negatives check + exception
 *
 * REFACTOR: extracted DELIMITERS constant, replaced for loop with streams
 */

public class StringCalculator {
	
	public int add(String numbers) {
		if(numbers.isEmpty()) {
			return 0;
		}
		
		String[] parts = numbers.split("[,\n]");
		
		List<Integer> negatives = Arrays.stream(parts)
									.map(Integer::parseInt)
									.filter(n -> n < 0)
									.collect(Collectors.toList());
		
		 if (!negatives.isEmpty()) {
	            throw new IllegalArgumentException(
	                "Negatives not allowed: " + negatives
	            );
	        }
		
		return Arrays.stream(parts)
				.mapToInt(Integer::parseInt)
				.sum();
		
		
	}

}
