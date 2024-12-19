package kr.where.backend.cluster.exception;

import kr.where.backend.exception.CustomException;

public class ClusterException extends CustomException {
    public ClusterException(final ClusterErrorCode clusterErrorCode) {
        super(clusterErrorCode);
    }

    public static class InvalidPathVariable extends ClusterException {
        public InvalidPathVariable() {
          super(ClusterErrorCode.INVALID_PATH_VARIABLE);
        }
    }
}
