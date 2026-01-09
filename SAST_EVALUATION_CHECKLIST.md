# SAST Tool Evaluation Checklist

Use this checklist when evaluating SAST tools with the Book Management System.

## Tool Information
- **Tool Name**: ___________________________
- **Version**: ___________________________
- **Evaluation Date**: ___________________________
- **Evaluator**: ___________________________

---

## Part 1: True Positives (Should Be Detected)

### Backend - Critical Vulnerabilities

- [ ] **SQL Injection** (BookController.java:67)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Path Traversal** (BookController.java:84)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Command Injection** (BookController.java:96)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Insecure Deserialization** (BookController.java:108)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

### Backend - High Severity Vulnerabilities

- [ ] **Hardcoded Credentials** (BookController.java:61-63)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **XML External Entity (XXE)** (BookController.java:119)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Cross-Site Scripting (XSS)** (BookController.java:146)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

### Backend - Medium Severity Vulnerabilities

- [ ] **Information Disclosure** (BookController.java:87)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Weak Cryptography** (BookController.java:133)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Insecure Random** (BookController.java:154)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Race Condition** (BookController.java:161)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

### Backend - Low Severity Vulnerabilities

- [ ] **Overly Permissive CORS** (BookController.java:15)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Format String Vulnerability** (BookController.java:175) [Subtle]
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

### Frontend Vulnerabilities

- [ ] **Missing Input Validation** (book.service.ts:37)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Path Traversal Risk** (book.service.ts:43)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Command Injection Risk** (book.service.ts:49)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Missing Format Validation - ISBN** (book-form.component.html:26)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Missing Range Validation - Price** (book-form.component.html:36)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

- [ ] **Missing Length Validation - Description** (book-form.component.html:53)
  - Detected: Yes / No
  - Severity Reported: ___________
  - Notes: _________________________________

---

## Part 2: False Positives (Should NOT Be Detected as Vulnerabilities)

### Backend Safe Code

- [ ] **JPA Native Query** (BookRepository.java:14)
  - Incorrectly Flagged: Yes / No
  - If flagged, severity: ___________
  - Notes: _________________________________

- [ ] **getAllBooks()** (BookController.java:28)
  - Incorrectly Flagged: Yes / No
  - If flagged, reason: _________________________________

- [ ] **getBookById()** (BookController.java:33)
  - Incorrectly Flagged: Yes / No
  - If flagged, reason: _________________________________

- [ ] **createBook()** (BookController.java:39)
  - Incorrectly Flagged: Yes / No
  - If flagged, reason: _________________________________

- [ ] **updateBook()** (BookController.java:45)
  - Incorrectly Flagged: Yes / No
  - If flagged, reason: _________________________________

- [ ] **deleteBook()** (BookController.java:53)
  - Incorrectly Flagged: Yes / No
  - If flagged, reason: _________________________________

- [ ] **BookService Methods** (All methods are safe)
  - Incorrectly Flagged: Yes / No
  - If flagged, which methods: _________________________________

### Frontend Safe Code

- [ ] **Hardcoded localhost URL** (book.service.ts:10)
  - Incorrectly Flagged: Yes / No (acceptable in dev)
  - Severity: ___________
  - Notes: _________________________________

- [ ] **Angular Template Interpolation** (book-list.component.html:22)
  - Incorrectly Flagged: Yes / No (Angular auto-sanitizes)
  - Notes: _________________________________

- [ ] **Standard HTTP CRUD Operations** (book.service.ts:14-34)
  - Incorrectly Flagged: Yes / No
  - If flagged, which methods: _________________________________

---

## Part 3: Tool Capabilities

### Detection Quality
- Provides line numbers: Yes / No
- Provides code snippets: Yes / No
- Explains vulnerability: Yes / No
- Suggests remediation: Yes / No
- Links to CWE/OWASP: Yes / No

### False Positive Handling
- Allows suppression: Yes / No
- Customizable rules: Yes / No
- Severity adjustment: Yes / No

### Reporting
- Generates report: Yes / No
- Export formats: _________________________________
- Integrates with CI/CD: Yes / No

### Language Support
- Java Support: Good / Fair / Poor
- TypeScript Support: Good / Fair / Poor
- Angular Awareness: Good / Fair / Poor
- Spring Boot Awareness: Good / Fair / Poor

---

## Part 4: Metrics Calculation

### Raw Counts
- True Positives Detected: ______ / 19
- False Positives: ______ 
- False Negatives (Missed): ______ 
- True Negatives Respected: ______ / 15+

### Calculated Metrics

**True Positive Rate (Recall)**
```
TPR = TP / (TP + FN) = ______ / 19 = ______%
```

**False Positive Rate**
```
FPR = FP / (FP + TN) = ______ / (______ + 15) = ______%
```

**Precision**
```
Precision = TP / (TP + FP) = ______ / (______ + ______) = ______%
```

**F1 Score**
```
F1 = 2 * (Precision * Recall) / (Precision + Recall) = ______%
```

**Accuracy**
```
Accuracy = (TP + TN) / Total = (______ + ______) / 34+ = ______%
```

---

## Part 5: Overall Assessment

### Strengths
1. _____________________________________________
2. _____________________________________________
3. _____________________________________________

### Weaknesses
1. _____________________________________________
2. _____________________________________________
3. _____________________________________________

### Performance
- Scan time: _____________ seconds/minutes
- Memory usage: _____________ MB
- CPU usage: Low / Medium / High

### Usability
- Ease of setup: Easy / Medium / Difficult
- Ease of use: Easy / Medium / Difficult
- Documentation quality: Good / Fair / Poor

### Integration
- CI/CD integration: Excellent / Good / Fair / Poor
- IDE integration: Excellent / Good / Fair / Poor

---

## Part 6: Final Grading

Based on F1 Score:

- [ ] **Grade A (95-100%)**: Excellent detection, minimal false positives
- [ ] **Grade B (85-94%)**: Good detection, few false positives
- [ ] **Grade C (70-84%)**: Acceptable detection, some issues
- [ ] **Grade D (60-69%)**: Poor detection, many missed vulnerabilities
- [ ] **Grade F (<60%)**: Inadequate for security testing

### Recommendation
- [ ] Highly Recommended
- [ ] Recommended
- [ ] Acceptable for basic use
- [ ] Not Recommended
- [ ] Not Suitable

### Overall Rating: ______ / 10

---

## Additional Notes

_________________________________________________________________
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________

---

## Comparison with Other Tools

If comparing multiple tools, fill this out:

| Metric | Tool 1 | Tool 2 | Tool 3 |
|--------|--------|--------|--------|
| True Positives | ______ | ______ | ______ |
| False Positives | ______ | ______ | ______ |
| F1 Score | ______% | ______% | ______% |
| Scan Time | ______ | ______ | ______ |
| Grade | ______ | ______ | ______ |

**Best Tool**: _______________________

**Reason**: _________________________________________________________________

---

**Evaluation Completed By**: ___________________________
**Date**: ___________________________
**Signature**: ___________________________
