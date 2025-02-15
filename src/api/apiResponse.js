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
  AUTH_2: 'AUTH-2',
  AUTH_3: 'AUTH-3',
  // 유저
  USER_0: 'USER-0',
  // 외부
  EXTERNAL_0: 'EXTERNAL-0',
  EXTERNAL_1: 'EXTERNAL-1',
  EXTERNAL_2: 'EXTERNAL-2',
  EXTERNAL_3: 'EXTERNAL-3',
  // POST
  POST_CATEGORY_0: 'POST-CATEGORY-0',
  POST_1: 'POST-1',
  POST_0: 'POST-0',
  // Tag
  TAG_1: 'TAG-1',
  // File
  FILE_0: 'FILE-0',
  // 댓글
  COMMENT_0: 'COMMENT-0'
});

export const handleOnOffPostLike = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.S_0:
      modalContents = createCustomResponse(
        ApiResponse.S_0,
        '처리되었습니다.',
        true
      );
      break;
    case ApiResponse.POST_0:
      modalContents = createCustomResponse(
        ApiResponse.POST_0,
        '게시글을 찾을 수 없습니다.'
      );
      break;
  }
  return modalContents;
};

export const handleJoin = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.EXTERNAL_0:
      modalContents = createCustomResponse(
        ApiResponse.EXTERNAL_0,
        '로그인/회원가입 중 외부 서비스의 권한 문제가 발생했습니다.'
      );
      break;
    case ApiResponse.EXTERNAL_1:
      modalContents = createCustomResponse(
        ApiResponse.EXTERNAL_0,
        '로그인/회원가입 중 외부 서비스의 요청 문제가 발생하였습니다.'
      );
      break;
    case ApiResponse.EXTERNAL_2:
      modalContents = createCustomResponse(
        ApiResponse.EXTERNAL_0,
        '로그인/회원가입 중 외부 서비스의 문제가 발생하였습니다.'
      );
      break;
  }
  return modalContents;
};

export const handleProfileImage = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.S_0:
      modalContents = createCustomResponse(
        ApiResponse.S_0,
        '변경완료되었습니다.',
        true
      );
      break;
  }
  return modalContents;
};

export const handleUserInfo = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.S_0:
      modalContents = createCustomResponse(
        ApiResponse.S_0,
        '변경완료되었습니다.',
        true
      );
      break;
    case ApiResponse.AUTH_0:
      modalContents = createCustomResponse(
        ApiResponse.AUTH_0,
        '요청한 유저정보를 찾을 수 없습니다.'
      );
      break;
    case ApiResponse.USER_0:
      modalContents = createCustomResponse(
        ApiResponse.USER_0,
        '중복된 닉네임입니다.'
      );
      break;
  }
  return modalContents;
};

export const handlePostRegister = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.S_0:
      modalContents = createCustomResponse(
        ApiResponse.S_0,
        '성공적으로 등록되었습니다. 등록된 글을 확인해보세요.',
        true
      );
      break;
    case ApiResponse.POST_CATEGORY_0:
      modalContents = createCustomResponse(
        ApiResponse.POST_CATEGORY_0,
        '카테고리를 잘못 선택하셨습니다.'
      );
      break;
    case ApiResponse.TAG_1:
      modalContents = createCustomResponse(
        ApiResponse.TAG_1,
        '태그를 잘못 선택하셨습니다.'
      );
      break;
  }
  return modalContents;
};

export const handleCommentRegister = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.S_0:
      modalContents = createCustomResponse(
        ApiResponse.S_0,
        '댓글등록완료!',
        true
      );
      break;
    case ApiResponse.POST_0:
      modalContents = createCustomResponse(
        ApiResponse.POST_0,
        '게시글을 찾을 수 없습니다.'
      );
      break;
    case ApiResponse.COMMENT_0:
      modalContents = createCustomResponse(
        ApiResponse.POST_0,
        '댓글을 찾을 수 없습니다.'
      );
      break;
  }
  return modalContents;
};

export const handlePostDelete = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.S_0:
      modalContents = createCustomResponse(
        ApiResponse.S_0,
        '성공적으로 삭제되었습니다.',
        true
      );
      break;
    case ApiResponse.POST_1:
      modalContents = createCustomResponse(
        ApiResponse.POST_1,
        '작성자가 아니거나 삭제할 수 없는 권한을 가지고 있습니다.'
      );
      break;
  }
  return modalContents;
};

export const handleUploadFile = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.FILE_0:
      modalContents = createCustomResponse(
        ApiResponse.FILE_0,
        '지원하지 않는 확장자입니다.'
      );
      break;
  }
  return modalContents;
};

export const handleCommonError = (response) => {
  let modalContents;
  switch (response.code) {
    case ApiResponse.AUTH_2:
      modalContents = createCustomResponse(
        ApiResponse.AUTH_2,
        '로그인이 필요한 서비스입니다.'
      );
      break;
    case ApiResponse.AUTH_3:
      modalContents = createCustomResponse(
        ApiResponse.AUTH_3,
        '접근권한이 없습니다.'
      );
      break;
    case ApiResponse.COMMON_0:
      modalContents = createCustomResponse(
        ApiResponse.COMMON_0,
        '필요한 정보가 올바르지 않습니다. 다시 확인해주세요.'
      );
      break;
    case ApiResponse.COMMON_1:
      modalContents = createCustomResponse(
        ApiResponse.COMMON_1,
        '죄송합니다. 원인을 알 수 없는 문제가 발생했습니다. 고객센터에 문의 바랍니다.'
      );
      break;
    case ApiResponse.COMMON_3:
      modalContents = createCustomResponse(
        ApiResponse.COMMON_3,
        '죄송합니다. 원인을 알 수 없는 문제가 발생했습니다. 고객센터에 문의 바랍니다.'
      );
      break;
    case ApiResponse.COMMON_4:
      modalContents = createCustomResponse(
        ApiResponse.COMMON_4,
        '죄송합니다. 원인을 알 수 없는 문제가 발생했습니다. 고객센터에 문의 바랍니다.'
      );
      break;
  }
  return modalContents;
};

export const customResponse = (code, message, success) => {
  return createCustomResponse(code, message, success);
};

const createCustomResponse = (code, message, success = false) => {
  return {
    isStatus: success,
    code: code,
    message: message
  };
};
