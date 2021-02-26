# This code is sponsored by Aperture Science Consumer Advocate Gabriel Fergesen, who would like to remind you that
# turrets are your friends.

# params that matter:  MaxArea --> Controls Size for Detection Canadates
# lower_red and upper_red: --> Use StoreFront UI to train these... trip out on color values, man and doink with the sliders.
# cap- needs to be set to whatever the index of the camera is... how to fix this will vary.

import cv2
import numpy as np


def nothing(x):
    # any operation
    pass

    # CAUTION, THIS NUMBER MUST BE CHECKED AFTER EVERY BOOT.
cap = cv2.VideoCapture(2)

cv2.namedWindow("Trackbars")
cv2.createTrackbar("L-H", "Trackbars", 0, 255, nothing)
cv2.createTrackbar("L-S", "Trackbars", 0, 255, nothing)
cv2.createTrackbar("L-V", "Trackbars", 0, 255, nothing)
cv2.createTrackbar("U-H", "Trackbars", 255, 255, nothing)
cv2.createTrackbar("U-S", "Trackbars", 255, 255, nothing)
cv2.createTrackbar("U-V", "Trackbars", 255, 255, nothing)

font = cv2.FONT_HERSHEY_COMPLEX

while True:
    _, frame = cap.read()
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    l_h = cv2.getTrackbarPos("L-H", "Trackbars")
    l_s = cv2.getTrackbarPos("L-S", "Trackbars")
    l_v = cv2.getTrackbarPos("L-V", "Trackbars")
    u_h = cv2.getTrackbarPos("U-H", "Trackbars")
    u_s = cv2.getTrackbarPos("U-S", "Trackbars")
    u_v = cv2.getTrackbarPos("U-V", "Trackbars")

    lower_red = np.array([l_h, l_s, l_v])
    upper_red = np.array([u_h, u_s, u_v])

    mask = cv2.inRange(hsv, lower_red, upper_red)
    kernel = np.ones((5, 5), np.uint8)
    mask = cv2.erode(mask, kernel)
    # Contours Detection

    # values for Cam Calibrated Goal detection: L-H = 95, L-S = 105, L-V = 000, U-H = 111, U-S = 255, U-V = 255
    contours,  _ = cv2.findContours(mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    for cnt in contours:
        area = cv2.contourArea(cnt)
        approx = cv2.approxPolyDP(cnt, 0.02*cv2.arcLength(cnt, True), True)
        x = approx.ravel() [0]
        y = approx.ravel() [1]

        if area > 400:
            cv2.drawContours(frame, [approx], 0, (0, 0, 0), 5)
            if len(approx) == 3:
                cv2.putText(frame, "triangle", (x, y), font, 1, (22, 100, 100))
            elif len(approx) == 4:
                cv2.putText(frame, "rectangle", (x, y), font, 1, (22, 100, 100))
            elif 10 < len(approx) < 20:
                cv2.putText(frame, "circle", (x, y), font, 1, (22, 100, 100))
            elif len(approx) == 8:
                cv2.putText(frame, "goal", (x, y), font, 1, (22, 100, 100))





    cv2.imshow('Frame', frame)
    cv2.imshow("Mask", mask)
    cv2.imshow("Kernel", kernel)

    key = cv2.waitKey(1)
    if key == 27:
        break

cap.release()
cv2.destroyAllWindows()
