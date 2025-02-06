package kr.where.backend.location;

import java.util.Objects;
import kr.where.backend.cluster.ClusterConstant;
import kr.where.backend.cluster.exception.ClusterException;
import org.springframework.stereotype.Component;

@Component
public class LocationUtils {

    public int getPercentage(int devidend, int devisor) {
        if (devisor == 0)
            return 0;
        return (int) (((double) devidend / devisor) * 100);
    }

    public void validateCluster(final String clusterZone) {
		//포맷형식이 맞는가? ex: "cx1" or "c1"
		if (!isValidFormat(clusterZone)) {
			throw new ClusterException.InvalidPathVariable();
		}

		//"c1"에서 "c" 추출
		final String prefix = extractPrefix(clusterZone);
		//"c1"에서 "1"을 추출
		final int clusterNumber = extractClusterNumber(clusterZone);

		//클러스터 숫자범위가 유효한가? ex: "c7"은 에러이다.
		if (!isValidClusterRange(prefix, clusterNumber)) {
			throw new ClusterException.InvalidPathVariable();
		}
    }

    public boolean isValidFormat(final String clusterZone) {
        return clusterZone.matches(ClusterConstant.CLUSTER_REGEX.getStringValue());
    }

    public String extractPrefix(final String clusterZone) {
        return clusterZone.replaceAll(ClusterConstant.ALL_NUMBER.getStringValue(), ClusterConstant.EMPTY_STRING.getStringValue());
    }

    public int extractClusterNumber(final String clusterZone) {
        return Integer.parseInt(clusterZone.replaceAll(ClusterConstant.ALL_STRING.getStringValue(), ClusterConstant.EMPTY_STRING.getStringValue()));
    }

    public boolean isValidClusterRange(final String prefix, final int clusterNumber) {
        if (Objects.equals(ClusterConstant.CLUSTER_C.getStringValue(), prefix)) {
            return clusterNumber >= ClusterConstant.CLUSTER_C.getMinValue() && clusterNumber <= ClusterConstant.CLUSTER_C.getMaxValue();
        }
        if (Objects.equals(ClusterConstant.CLUSTER_CX.getStringValue(), prefix)) {
            return clusterNumber >= ClusterConstant.CLUSTER_CX.getMinValue() && clusterNumber <= ClusterConstant.CLUSTER_CX.getMaxValue();
        }
        return false;
    }
}
