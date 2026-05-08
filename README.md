# FTC 33748 Decode Season
This repository contains the code for FTC Team 33748’s Decode season robot, Hungry Hippo, which earned two Control Awards in the Utah region. The robot has also advanced to the Michiania premier event.

## Information
Overview

The codebase is primarily driver-oriented, with advanced features including autonomous cycling during teleop. Using odometry and path planning via PedroPathing, the robot can align with scoring targets quickly and consistently.



The alignment system went through several iterations:

Logitech C270 webcam - Limited by narrow field of view and slow performance of the hub's VisionPortal
HuskyLens (v1) - Similar limitations in responsiveness and accuracy
NexiGo Q60 webcam - Improved with a 110° FOV, but still constrained by AprilTag update rates via VisionPortal
Sensor fusion approach - Combined camera heading targets with drivetrain heading commonly seen in FRC, though this was too slow still
Pinpoint (v1) - Provided fast, reliable position and heading updates, which drastically improved alignment

[https://www.youtube.com/watch?v=ttxTpN796AM
](https://www.youtube.com/watch?v=ttxTpN796AM)

You can  see how the robot moves with the autocycles in this robot POV youtube video. We did miss several because our partner pushed the goal up and we kept hitting the rim because of that, but its quite consistant. It is important to note this is an earlier iteration of the autocycling that has it returning to the human player zone and intaking some laid out artifacts after it shoots 3 times, but since we are now using a wheel instead of a CAM its not exactly precise how many balls we have shot, so we don't do it that way anymore.


Left stick – Translation
Right stick (left/right) – Rotation
Right bumper – Shoot
Left trigger – Intake
A button – Far cycle
B button – Mid cycle

All constants you may need to touch are in the constants folder of the project, in a file for their respective subsystem. (`TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants`)

