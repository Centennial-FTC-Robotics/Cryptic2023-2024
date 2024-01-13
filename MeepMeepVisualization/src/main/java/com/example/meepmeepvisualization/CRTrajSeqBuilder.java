package com.example.meepmeepvisualization;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

public class CRTrajSeqBuilder {

    private enum RandomizationPos {
        INNER,
        CENTER,
        OUTER
    }

    public static double BACKDROP_INNER = 29.4;
    public static double BACKDROP_CENTER = 35.4;
    public static double BACKDROP_OUTER = 41.4;
    public static double BACKDROP_DISTANCE = 52;
    public static double STACK_X = -59;
    public static double STACK_Y = 11.5;
    public static double ROBOT_START_Y = 2.5*23.5;


    private TrajectorySequenceBuilder seq;
    private Globals.Alliance team;
    private Globals.StartLoc startLoc;

    public static CRTrajSeqBuilder init(
            DriveShim drive,
            Globals.Alliance team,
            Globals.StartLoc startLoc
    ) {
        return new CRTrajSeqBuilder(drive, team, startLoc);
    }

    public CRTrajSeqBuilder(
            DriveShim drive,
            Globals.Alliance team,
            Globals.StartLoc startLoc
    ) {
        this.team = team;
        this.startLoc = startLoc;

        int mult = (team == Globals.Alliance.RED) ? -1 : 1;
        double startX = (startLoc == Globals.StartLoc.FRONTSTAGE) ? -1.5*23.5 : 0.5*23.5;

        Pose2d startPos = new Pose2d(startX, mult*ROBOT_START_Y, Math.toRadians(mult*270));
        seq = drive.trajectorySequenceBuilder(startPos);
    }

    private RandomizationPos getRandomizationPos(ElementProcessor.PropPositions propPos) {

        RandomizationPos backdropTarget;

        if(propPos == ElementProcessor.PropPositions.MIDDLE) {
            backdropTarget = RandomizationPos.CENTER;
        } else if(
                (propPos == ElementProcessor.PropPositions.LEFT && team == Globals.Alliance.BLUE)
                        || (propPos == ElementProcessor.PropPositions.RIGHT && team == Globals.Alliance.RED)
        ){
            backdropTarget = RandomizationPos.OUTER;
        } else {
            backdropTarget = RandomizationPos.INNER;
        }

        return backdropTarget;
    }

    public CRTrajSeqBuilder purpleDepositFrontstage(
            ElementProcessor.PropPositions propPos, boolean prepStack
    ) {
        RandomizationPos randPos = getRandomizationPos(propPos);

        int mult = (team == Globals.Alliance.RED) ? -1 : 1;

        if(randPos == RandomizationPos.INNER) {

            seq.splineToConstantHeading(new Vector2d(-47.45, 37.48*mult), Math.toRadians(mult*270.00))
                    .addTemporalMarker(() -> {
//                        intake.setHeight(5);
                    });

            if(prepStack) {
                seq.setReversed(true)
                        .splineToConstantHeading(new Vector2d(-35.2, 34.76*mult), Math.toRadians(270.00*mult))
//                    .setReversed(false)
                        .splineTo(new Vector2d(-42.69, 12.54*mult), Math.toRadians(220.60*mult))
                        .addTemporalMarker(() -> {
//                        intake.setNoodlePower(0.8);
//                        outtake.setWheel(-1* Outtake.wheelOutDir);
                        })
                        .splineTo(new Vector2d(STACK_X, mult*STACK_Y), Math.toRadians(180.00))
                        .addTemporalMarker(() -> {
//                            intake.setHeight(4);
                        });
//                    .setReversed(true);
            }


        } else if(randPos == RandomizationPos.CENTER) {

            seq.splineToLinearHeading(new Pose2d(-39.07, mult*29.83,
                    Math.toRadians(mult*320.00)), Math.toRadians(mult*270.00))
                    .addTemporalMarker(() -> {
//                        intake.setHeight(5);
                    });
            if(prepStack) {
                seq.setReversed(true)
                        .splineToConstantHeading(new Vector2d(-51.63, mult*21.02), Math.toRadians(mult*320.00))
//                                .setReversed(false)

                        .splineTo(new Vector2d(-53.00, STACK_Y*mult), Math.toRadians(mult*180.00))
                        .addTemporalMarker(() -> {
//                        intake.setNoodlePower(0.8);
//                        outtake.setWheel(-1* Outtake.wheelOutDir);
                        })
                        .splineTo(new Vector2d(STACK_X, STACK_Y*mult), Math.toRadians(mult*180.00))
                        .addTemporalMarker(() -> {
//                        intake.setHeight(4);
                        });
            }

        } else {

            seq.splineTo(new Vector2d(-31.02, 37.71*mult), Math.toRadians(-35.00*mult))
                    .addTemporalMarker(() -> {
//                        intake.setHeight(5);
                    });

            if(prepStack) {
                seq.setReversed(true)
                        .UNSTABLE_addTemporalMarkerOffset(2, () -> {
//                        intake.setNoodlePower(0.8);
//                        outtake.setWheel(-1* Outtake.wheelOutDir);
                        })
                        .splineToLinearHeading(new Pose2d(STACK_X, mult*STACK_Y,
                                Math.toRadians(mult*180.00)), Math.toRadians(mult*180))
                        .addTemporalMarker(() -> {
//                        intake.setHeight(4);
                        });

            }

        }


        return this;
    }

    public CRTrajSeqBuilder purpleYellowBackstage(
            ElementProcessor.PropPositions propPos) {

        RandomizationPos randPos = getRandomizationPos(propPos);
        int mult = (team == Globals.Alliance.RED) ? -1 : 1;
        double targetY;

        if(randPos == RandomizationPos.OUTER) {
            targetY = mult * BACKDROP_OUTER;
        } else if(randPos == RandomizationPos.CENTER) {
            targetY = mult * BACKDROP_CENTER;
        } else {
            targetY = mult * BACKDROP_INNER;
        }

        if(randPos == RandomizationPos.INNER) {
            seq.splineTo(new Vector2d(8.92, mult*35.95), Math.toRadians(mult*200.00));
        } else if(randPos == RandomizationPos.CENTER) {
            seq.splineToLinearHeading(new Pose2d(15.74, mult*33.25,
                    Math.toRadians(245.00*mult)), Math.toRadians(270.00*mult));
        } else {
            seq.splineToLinearHeading(new Pose2d(
                    31.76, mult*31.76, Math.toRadians(mult*180.00)),
                    Math.toRadians(mult*225.00));
        }

        seq.addTemporalMarker(() -> {
//                    intake.setHeight(5);
                })
                .setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0.25, () -> {
//                    outtake.incrementSlidePos(1);
                })
                .splineTo(new Vector2d(BACKDROP_DISTANCE, targetY), 0)
                .addTemporalMarker(() -> {
//                    outtake.setWheel(Outtake.wheelOutDir);
                })
                .waitSeconds(0.7)
                .setReversed(false);


        return this;

    }

    public CRTrajSeqBuilder scoreYellowFrontstage(
//            Intake intake,
//            Outtake outtake,
            ElementProcessor.PropPositions propPos
    ) {
        RandomizationPos randPos = getRandomizationPos(propPos);

        int mult = (team == Globals.Alliance.RED) ? -1 : 1;

        double targetY;

        if(randPos == RandomizationPos.OUTER) {
            targetY = mult * BACKDROP_OUTER;
        } else if(randPos == RandomizationPos.CENTER) {
            targetY = mult * BACKDROP_CENTER;
        } else {
            targetY = mult * BACKDROP_INNER;
        }

        seq.setReversed(false)
                .setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0.6, () -> {
//                    intake.setHeight(0);
                })
                .UNSTABLE_addTemporalMarkerOffset(1.1, () -> {
//                    intake.setNoodlePower(0);
                })
                .splineTo(new Vector2d(25.00, STACK_Y*mult), Math.toRadians(0.00))
                .UNSTABLE_addTemporalMarkerOffset(0.15, () -> {
//                    outtake.incrementSlidePos(1);
                })
                .splineToConstantHeading(new Vector2d(BACKDROP_DISTANCE, targetY), Math.toRadians(0.00))
                .addTemporalMarker(() -> {
//                    outtake.setWheel(Outtake.wheelOutDir);
                })
                .waitSeconds(0.7)
                .setReversed(false);
        return this;
    }

    public CRTrajSeqBuilder scoreFromStack(
//            Intake intake,
//            Outtake out
    ) {

        int mult = (team == Globals.Alliance.RED) ? -1 : 1;

        seq.setReversed(true)
                .UNSTABLE_addTemporalMarkerOffset(0.6, () -> {
//                    intake.setHeight(0);
                })
                .UNSTABLE_addTemporalMarkerOffset(1.1, () -> {
//                    intake.setNoodlePower(0);
                })
                .splineTo(new Vector2d(25.00, STACK_Y*mult), Math.toRadians(0.00))
                .UNSTABLE_addTemporalMarkerOffset(0.15, () -> {
//                    outtake.incrementSlidePos(1);
                })
                .splineToConstantHeading(new Vector2d(BACKDROP_DISTANCE, mult*BACKDROP_INNER), Math.toRadians(0.00))
                .addTemporalMarker(() -> {
//                    outtake.setWheel(Outtake.wheelOutDir);
                })
                .waitSeconds(0.7)
                .setReversed(false);

        return this;
    }

    public CRTrajSeqBuilder returnToIntakeStack(
//            Intake intake,
//            Outtake outtake,
            int oneHigherIntakeHeight
    ) {

        int mult = (team == Globals.Alliance.RED) ? -1 : 1;

        seq.setReversed(false)
                .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                    outtake.retractSlides();
//                    outtake.setWheel(0);
                })
                .UNSTABLE_addTemporalMarkerOffset(3.3, () -> {
//                    intake.setHeight(oneHigherIntakeHeight);
//                    intake.setNoodlePower(0.8);
//                    outtake.setWheel(-1* Outtake.wheelOutDir);
                })
                .splineToConstantHeading(new Vector2d(25.00, STACK_Y*mult), Math.toRadians(mult*180))
                .splineTo(new Vector2d(STACK_X, STACK_Y*mult), Math.toRadians(mult*180))
                .addTemporalMarker(() -> {
//                    intake.setHeight(oneHigherIntakeHeight-1);
                })
                .UNSTABLE_addTemporalMarkerOffset(0.35, () -> {
//                    intake.setHeight(oneHigherIntakeHeight-2);
                })
                .waitSeconds(0.7);

        return this;
    }

    public CRTrajSeqBuilder park(boolean full) {
        int mult = (team == Globals.Alliance.RED) ? -1 : 1;

        if(!full) {
            seq.setReversed(false)
                    .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
//                        outtake.setWheel(0);
//                        outtake.retractSlides();
                    })
                    .forward(6);
            return this;
        }

        seq.setReversed(false)
                .UNSTABLE_addTemporalMarkerOffset(0.4, () -> {
//                    outtake.retractSlides();
//                    outtake.setWheel(0);
                })
                .splineToConstantHeading(new Vector2d(BACKDROP_DISTANCE, mult*60), Math.toRadians(0.00))
                .splineToConstantHeading(new Vector2d(62, 62*mult), Math.toRadians(1.21*mult));

        return this;
    }

    public TrajectorySequence build() {
        return seq.build();
    }



}
