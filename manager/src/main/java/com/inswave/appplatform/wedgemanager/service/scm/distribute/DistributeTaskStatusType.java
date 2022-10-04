package com.inswave.appplatform.wedgemanager.service.scm.distribute;

public enum DistributeTaskStatusType {
    WAIT,                           // 대기
    INPROGRESS_SCANNING,            // 진행1 (배포대상 스캔)
    INPROGRESS_BEFORE_CLEANING,     // 진행2 (배포대상 목적지 삭제)
    INPROGRESS_COPY,                // 진행3 (배포대상 목적지로 복사)
    INPROGRESS_RESULT_SAVE,         // 진행4 (배포결과 저장)
    INPROGRESS_UPDATE_VERSION,      // 진행5 (배포대상 버전업)
    COMPLETED                       // 완료 (실제로는 미사용 : 완료이면 큐에서 사라질테니.)
}
