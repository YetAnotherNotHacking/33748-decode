package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.constants.HardwareConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class VisionSubsystem {
    public static final int BLUE_TAG_ID = 20;
    public static final int RED_TAG_ID = 24;

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTagProcessor;

    public void init(HardwareMap hardwareMap) {
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, HardwareConstants.TAG_CAMERA))
                .setCameraResolution(new Size(1920, 1080))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(aprilTagProcessor)
                .build();
    }

    public AprilTagDetection getDetectionById(int tagId) {
        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
        if (detections == null || detections.isEmpty()) {
            return null;
        }

        for (AprilTagDetection detection : detections) {
            if (detection.id == tagId) {
                return detection;
            }
        }

        return null;
    }

    public AprilTagDetection getBlueDetection() {
        return getDetectionById(BLUE_TAG_ID);
    }

    public AprilTagDetection getRedDetection() {
        return getDetectionById(RED_TAG_ID);
    }

    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}
