package org.photonvision.vision.pipeline;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AprilTagPipelineSettings")
public class CustomTestPipelineSettings extends AdvancedPipelineSettings {
    public int test1 = 1;
    public int test2 = 2;
    public int test3 = 3;

    public CustomTestPipelineSettings() {
        super();

    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp1;
        temp1 = Double.doubleToLongBits(test1);
        result = prime * result + (int) (temp1 ^ (temp1 >>> 32));
        long temp2;
        temp2 = Double.doubleToLongBits(test1);
        result = prime * result + (int) (temp2^ (temp2 >>> 32));
        long temp3;
        temp3 = Double.doubleToLongBits(test1);
        result = prime * result + (int) (temp3 ^ (temp3 >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        CustomTestPipelineSettings other = (CustomTestPipelineSettings) obj;
        if(test1 != ((CustomTestPipelineSettings) obj).test1) return false;
        if(test2 != ((CustomTestPipelineSettings) obj).test2) return false;
        if(test3 != ((CustomTestPipelineSettings) obj).test3) return false;
        return true;
    }
}
