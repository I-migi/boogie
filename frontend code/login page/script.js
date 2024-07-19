//양식과 입력 필드 요소들을 가져오기.
const form = document.getElementById('form');
//const username = document.getElementById('username');
//const email = document.getElementById('email');
//const password = document.getElementById('password');
//const password2 = document.getElementById('password2');
const id = document.querySelector('.id-rec');
const password = document.querySelector('.pw-rec');

//로컬 스토리지에서 사용자 정보 가져오기: 
//로컬 스토리지에 저장된 사용자 정보를 JSON 형식으로 가져와 파싱.
/*const getSavedUserInfos = () => {
  const userInfosJSON = localStorage.getItem('userInfos');
  try {                                                           //저장된 정보가 없거나 JSON 파싱에 실패하면 빈 배열을 반환.
    return userInfosJSON ? JSON.parse(userInfosJSON) : [];
  } catch {
    return [];
  }
};


//로컬 스토리지에 사용자 정보 저장하기
const saveUserInfos = (userInfos) => {
  localStorage.setItem('userInfos', JSON.stringify(userInfos));     //사용자 정보를 JSON 형식으로 로컬 스토리지에 저장.
};*/


//에러 및 성공 메시지 표시 함수
//입력 필드에 에러 메시지나 성공 메시지를 표시. setError() : 에러 메시지를 설정, setSuccess() : 성공 메시지를 설정.
const setError = (element, message) => {               //setError() : 특정 입력 필드에 대해 에러 상태를 설정하고 에러 메시지를 표시
  const inputControl = element.parentElement;           //입력 필드의 부모 요소인 .container를 가져옴.
  const errorDisplay = inputControl.querySelector('.id-rec', '.pw-rec');        //부모 요소 내에서 .error 클래스를 가진 요소를 찾음.

  errorDisplay.innerText = message;           //에러 메시지 요소의 텍스트를 설정
  inputControl.classList.add('error');          //부모 요소에 error 클래스를 추가. 이 클래스는 보통 CSS를 통해 에러 상태를 시각적으로 표시함.
  inputControl.classList.remove('success');     //부모 요소에서 success 클래스를 제거. 성공 상태를 나타내는 클래스가 있을 경우 이를 제거하여 에러 상태만 남도록 함.
};

const setSuccess = (element) => {               //setSuccess() : 특정 입력 필드에 대해 성공 상태를 설정하고 에러 메시지를 제거.
  const inputControl = element.parentElement;               //입력 필드의 부모 요소인 .container를 가져옴.
  const errorDisplay = inputControl.querySelector('.id-rec','.pw-rec');         //부모 요소 내에서 .error 클래스를 가진 요소를 찾음.

  errorDisplay.innerText = '';                  //에러 메시지를 빈 문자열로 설정하여 메시지를 제거
  inputControl.classList.add('success');              //부모 요소에 'success' 클래스를 추가.
  inputControl.classList.remove('error');             //부모 요소에서 'error' 클래스를 제거.
};


//이메일 형식 유효성 검사
//정규 표현식 re를 사용해 이메일 형식이 올바른지 검증.
/*const isValidEmail = (email) => {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());         //변환된 이메일 주소가 정규 표현식 re와 일치하는지 테스트. 일치하면 true, 일치하지 않으면 false를 반환.
};*/


//필수 필드, 길이, 이메일, 비밀번호 일치 검증 함수
//각 함수는 입력 값이 유효한지 검사하고, 유효하지 않으면 에러 메시지를 표시.
const checkRequired = (inputArr) => {       //checkRequired() : 입력 필드 배열(inputArr)의 각 필드가 비어있는지 확인.
  let isValid = true;             //폼의 전체 유효성을 나타내는 플래그 변수를 초기화.
  inputArr.forEach(input => {         //배열의 각 입력 필드를 순회.
    if (input.value.trim() === '') {              //입력 필드의 값이 비어있다면(trim으로 공백을 제거한 후),
      setError(input, `${input.placeholder} is required`);            //에러 메시지를 설정하고 입력 필드의 부모 요소에 에러 클래스를 추가.
      isValid = false;            //폼의 유효성을 false로 설정.
    } else {                      //입력 필드의 값이 비어있지 않다면,
      setSuccess(input);          //성공 메시지를 설정하고 입력 필드의 부모 요소에 성공 클래스를 추가.
    }
  });
  return isValid;             //폼의 전체 유효성을 반환.
};

const checkLength = (input, min, max) => {
  let isValid = true;
  if (input.value.length < min) {
    setError(input, `${input.placeholder} must be at least ${min} characters`);
    isValid = false;
  } else if (input.value.length > max) {
    setError(input, `${input.placeholder} must be less than ${max} characters`);
    isValid = false;
  } else {
    setSuccess(input);
  }
  return isValid;
};

/*const checkEmail = (input) => {
  let isValid = true;
  if (!isValidEmail(input.value.trim())) {
    setError(input, 'Email is not valid');
    isValid = false;
  } else {
    setSuccess(input);
  }
  return isValid;
};
*/

/*const checkPasswordsMatch = (input1, input2) => {
  let isValid = true;
  if (input1.value !== input2.value) {
    setError(input2, 'Passwords do not match');
    isValid = false;
  } else {
    setSuccess(input2);
  }
  return isValid;
};*/


//양식 제출 이벤트 리스너
//양식 제출 시 각 필드를 검증하고, 모든 검증을 통과하면 새로운 사용자 정보를 로컬 스토리지에 저장. 
//저장 후에는 양식을 초기화하고 성공 메시지를 표시. uuid.v4()는 고유 사용자 ID를 생성하는 데 사용.
form.addEventListener('submit', (e) => {
  e.preventDefault();

  const isUsernameValid = checkRequired([username]) && checkLength(username, 3, 15);
  const isEmailValid = checkRequired([email]) && checkEmail(email);
  const isPasswordValid = checkRequired([password]) && checkLength(password, 6, 25);
  const isPassword2Valid = checkRequired([password2]) && checkPasswordsMatch(password, password2);

  const isFormValid = isUsernameValid && isEmailValid && isPasswordValid && isPassword2Valid;

  if (isFormValid) {
    const userId = uuid.v4();
    const userInfos = getSavedUserInfos();

    userInfos.push({
      id: userId,
      username: username.value,
      email: email.value,
      password: password.value
    });

    saveUserInfos(userInfos);
    alert('User registered successfully!');
    form.reset();
    document.querySelectorAll('.input-control').forEach(control => {
      control.classList.remove('success');
    });
  }
});