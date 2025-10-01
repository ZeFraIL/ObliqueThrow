package zeev.fraiman.obliquethrow;

import java.io.Serializable;

public class Exercise implements Serializable {

    private String V0;
    private String Angle;
    private String H0;
    private String HMax;
    private String LMax;
    private String TTotal;

    public Exercise(String V0, String Angle, String H0, String HMax, String LMax, String TTotal) {
        this.V0 = V0;
        this.Angle = Angle;
        this.H0 = H0;
        this.HMax = HMax;
        this.LMax = LMax;
        this.TTotal = TTotal;
    }

    public String getV0() {
        return V0;
    }

    public String getAngle() {
        return Angle;
    }

    public String getH0() {
        return H0;
    }

    public String getHMax() {
        return HMax;
    }

    public String getLMax() {
        return LMax;
    }

    public String getTTotal() {
        return TTotal;
    }
}
