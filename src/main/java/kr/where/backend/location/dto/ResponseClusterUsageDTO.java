package kr.where.backend.location.dto;

import lombok.Getter;

@Getter
public class ResponseClusterUsageDTO {
   private String name;
   private double usageRate;
   private int usingImacCount;
   private int totalImacCount;

   private ResponseClusterUsageDTO(final String name, final double usageRate, final int usingImacCount, final int totalImacCount) {
       this.name = name;
       this.usageRate = usageRate;
       this.usingImacCount = usingImacCount;
       this.totalImacCount = totalImacCount;
   }

   static public ResponseClusterUsageDTO of (final String name, final double usageRate, final int usingImacCount, final int totalImacCount) {
       return new ResponseClusterUsageDTO(name, usageRate, usingImacCount, totalImacCount);
   }
}
