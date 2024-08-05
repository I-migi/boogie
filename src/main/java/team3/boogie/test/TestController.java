package team3.boogie.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TestController {

	@GetMapping("/survey/child")
	public String childForm() {
		return "html/SelfDiagnosisChild";
	}

	@GetMapping("/survey/Infant")
	public String InfantForm() {
		return "html/SelfDiagnosisInfant";
	}

	@GetMapping("/survey")
	public String showSurveyPage() {
		return "html/SelfDiagnosis"; // 설문조사 페이지 렌더링
	}

	@PostMapping("/survey")
	public String processForm(@RequestParam Map<String, String> formData) {
		int checkedCount = 0;

		for (String key : formData.keySet()) {
			if ("on".equals(formData.get(key))) {
				checkedCount++;
			}
		}

		// 체크박스의 갯수에 따라 다른 페이지로 리다이렉트
		if (checkedCount >= 4) {
			return "redirect:/resultA"; // 4개 이상 체크한 경우 A 결과 페이지로 이동
		} else {
			return "redirect:/resultB"; // 4개 미만 체크한 경우 B 결과 페이지로 이동
		}
	}

	@GetMapping("/resultA")
	public String showResultAPage() {
		return "html/SelfDiagnosisResult"; // A 결과 페이지 렌더링
	}

	@GetMapping("/resultB")
	public String showResultBPage() {
		return "html/SelfDiagnosisResult2"; // B 결과 페이지 렌더링
	}
}
