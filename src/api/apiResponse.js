export const ApiResponse = Object.freeze({
  // 성공
  S_0: 'SUCCESS-0',
  // 공통
  COMMON_0: 'COMMON-0',
  COMMON_1: 'COMMON-1',
  COMMON_3: 'COMMON-3',
  COMMON_4: 'COMMON-4',
  // 권한
  AUTH_0: 'AUTH-0',
  // 유저
  USER_0: 'USER-0',
  USER_1: 'USER-1',
  USER_2: 'USER-2',
  USER_3: 'USER-3',
  USER_4: 'USER-4',
  // 외부
  EXTERNAL_0: 'EXTERNAL-0',
  EXTERNAL_1: 'EXTERNAL-1',
  EXTERNAL_2: 'EXTERNAL-2',
  EXTERNAL_3: 'EXTERNAL-3'
});

export const handleLogout = response => {
  switch (response.code) {
    case ApiResponse.S_0:
      response.message = '다음에도 NOGI와 함께하세요!😊';
      break;
  }
  return response;
};

export const handleUserInfo = response => {
  switch (response.code) {
    case ApiResponse.S_0:
      response.message = '정상적으로 등록완료했어요.';
      break;
    case ApiResponse.AUTH_0:
      response.message = '요청한 유저정보를 찾을 수 없습니다.';
      break;
  }
  return response;
};

export const handleCommonError = response => {
  let isCommonError = true;
  switch (response.code) {
    case ApiResponse.USER_2:
      response.message =
        '로그인이 필요해요.\n지금 가입하면 NOGI의 다양한 서비스를 무료로 즐길 수 있어요! 🚀';
      break;
    case ApiResponse.USER_3:
      response.message =
        '앗! 이 기능을 사용하려면 더 높은 권한이 필요해요 🚫\n[Home]으로 안내해 드릴게요';
      break;
    case ApiResponse.COMMON_0:
      response.message = '필요한 정보가 올바르지 않습니다. 다시 확인해주세요.';
      break;
    case ApiResponse.COMMON_1:
      response.message =
        '죄송합니다. 원인을 알 수 없는 문제가 발생했습니다. 고객센터에 문의 바랍니다.';
      break;
    case ApiResponse.COMMON_3:
      response.message =
        '죄송합니다. 원인을 알 수 없는 문제가 발생했습니다. 고객센터에 문의 바랍니다.';
      break;
    case ApiResponse.COMMON_4:
      response.message =
        '죄송합니다. 원인을 알 수 없는 문제가 발생했습니다. 고객센터에 문의 바랍니다.';
      break;
    default:
      isCommonError = false;
  }
  return isCommonError;
};

export const convertResponseFormat = response => {
  return {
    isSuccess: response.success,
    code: response.code,
    message: response.msg,
    result: response.result
  };
};
