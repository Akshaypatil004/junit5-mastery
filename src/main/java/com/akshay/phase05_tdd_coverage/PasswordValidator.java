// PasswordValidator.java
package com.akshay.phase05_tdd_coverage;


import java.util.ArrayList;
import java.util.List;

/**
 * PRACTICE SESSION 2 — Independent Practice
 * Phase 5: TDD - Password Validator
 *
 * Design decisions made:
 * - Static utility class (no need to instantiate)
 * - isValid() returns boolean for simple pass/fail
 * - getFailureReasons() communicates WHY it failed
 * - getStrengthScore() returns 0-4 score based on criteria met
 *
 * TDD cycle followed:
 * STEP 1 — RED:   test empty password throws exception  → failed
 *          GREEN: added isBlank() check + throw
 * Remaining steps done together, final two commits pushed.
 */
public class PasswordValidator {

    private static final int MINIMUM_LENGTH = 8;

    private PasswordValidator() {}

    public static boolean isValid(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty or blank");
        }
        return password.length() >= MINIMUM_LENGTH;
    }

    /**
     * Returns list of reasons why password is weak or invalid.
     * Empty list means password is fully valid.
     */
    public static List<String> getFailureReasons(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty or blank");
        }

        List<String> reasons = new ArrayList<>();

        if (password.length() < MINIMUM_LENGTH) {
            reasons.add("Too short — minimum " + MINIMUM_LENGTH + " characters required");
        }
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            reasons.add("Must contain at least one uppercase letter");
        }
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            reasons.add("Must contain at least one lowercase letter");
        }
        if (!password.chars().anyMatch(Character::isDigit)) {
            reasons.add("Must contain at least one digit");
        }
        if (!password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
            reasons.add("Must contain at least one special character");
        }

        return reasons;
    }

    /**
     * Returns strength score 0-4:
     * 0 = very weak, 4 = strong
     * Criteria: length OK, has uppercase, has lowercase, has digit, has special char
     */
    public static int getStrengthScore(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty or blank");
        }

        int score = 0;

        if (password.length() >= MINIMUM_LENGTH)              score++;
        if (password.chars().anyMatch(Character::isUpperCase)) score++;
        if (password.chars().anyMatch(Character::isLowerCase)) score++;
        if (password.chars().anyMatch(Character::isDigit))     score++;
        if (password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) score++;

        return score; // max 5
    }
}
