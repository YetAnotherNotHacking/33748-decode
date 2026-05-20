package org.firstinspires.ftc.teamcode.constants;

import com.pedropathing.geometry.Pose;
import com.bylazar.configurables.annotations.Configurable;

@Configurable
public final class AutonomousPoints {

    public static final class Red {
        public static final class Back {
            public static final Pose SHOOT = new Pose(7.79, -0.48, Math.toRadians(-109.00));
            public static final Pose INTAKE_CLOSE_1 = new Pose(26.00, -2.0, Math.toRadians(90.00));
            public static final Pose INTAKE_CLOSE_2 = new Pose(26.00, -39.70, Math.toRadians(90.00));
            public static final Pose INTAKE_MIDDLE_1 = new Pose(50.00, -2.0, Math.toRadians(90.00));
            public static final Pose INTAKE_MIDDLE_2 = new Pose(50.00, -34.57, Math.toRadians(90.00));
            public static final Pose INTAKE_HUMAN = new Pose(1.99, -44.98, Math.toRadians(70.94));
            public static final Pose CLOSE_PARK = new Pose(29.91, -36.08, Math.toRadians(-90.00));
        }

        public static final class Front {
            public static final Pose SHOOT = new Pose(-49.65, -1.28, Math.toRadians(-83.59));
            public static final Pose INTAKE_FAR_1 = new Pose(-40.99, -17.66, Math.toRadians(142.06));
            public static final Pose INTAKE_FAR_2 = new Pose(-22.61, -33.93, Math.toRadians(140.47));
            public static final Pose INTAKE_MID_1 = new Pose(-54.56, -34.25, Math.toRadians(140.66));
            public static final Pose INTAKE_MID_2 = new Pose(-38.80, -53.29, Math.toRadians(139.87));
            public static final Pose PARK_FAR = new Pose(-31.36, 17.58, Math.toRadians(-41.80));
        }
    }

    public static final class Blue {
        public static final class Back {
            public static final Pose SHOOT = new Pose(8.60, -3.27, Math.toRadians(-60.68));
            public static final Pose INTAKE_CLOSE_1 = new Pose(25.63, 13.98, Math.toRadians(-90.00));
            public static final Pose INTAKE_CLOSE_2 = new Pose(26.75, 36.23, Math.toRadians(-90.00));
            public static final Pose INTAKE_MIDDLE_1 = new Pose(48.41, 14.86, Math.toRadians(-90.00));
            public static final Pose INTAKE_MIDDLE_2 = new Pose(49.04, 36.17, Math.toRadians(-90.00));
            public static final Pose INTAKE_HUMAN = new Pose(4.47, 45.03, Math.toRadians(-71.32));
            public static final Pose CLOSE_PARK = new Pose(29.69, 35.17, Math.toRadians(90.00));
        }

        public static final class Front {
            public static final Pose SHOOT = new Pose(-53.81, 5.40, Math.toRadians(-94.78));
            public static final Pose INTAKE_FAR_1 = new Pose(-41.03, 18.64, Math.toRadians(-142.38));
            public static final Pose INTAKE_FAR_2 = new Pose(-23.03, 32.60, Math.toRadians(-140.29));
            public static final Pose INTAKE_MID_1 = new Pose(-57.38, 35.85, Math.toRadians(-139.75));
            public static final Pose INTAKE_MID_2 = new Pose(-36.30, 51.88, Math.toRadians(-140.56));
            public static final Pose PARK_FAR = new Pose(-32.70, -16.76, Math.toRadians(42.29));
        }
    }

    private AutonomousPoints() {}
}
