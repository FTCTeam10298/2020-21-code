# Calibrates the camera. All-around useful.


import numpy as np
import cv2
import glob  # termination criteria

criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

# SURVIVE THE DIVILS BELOW dirpath: The directory that we moved our images. prefix: Images should have the same name.
# This prefix represents that name. (If the list is: image1.jpg, image2.jpg … it shows that the prefix is “image”.
# Code is generalized but we need a prefix to iterate, otherwise, there can be any other file that we don’t care
# about.) image_format: “jpg” or“png”. These formats are supported by OpenCV. square_size: Edge size of one square.
# width: Number of intersection points of squares in the long side of the calibration board. It is 9 by default if
# you use the chessboard above. height: Number of intersection points of squares in the short side of the calibration
# board. It is 6by default if you use the chessboard above.


def calibrate(dirpath, prefix, image_format, square_size, width=9, height=6):
    """ Apply camera calibration operation for images in the given directory path. """
    # prepare object points, like (0,0,0), (1,0,0), (2,0,0) ....,(8,6,0)

    #objp is our chessboard matrix. We will initialize it with coordinates and multiply with our measurement, square size. It will become our map for the chessboard and represents how the board should be.

    objp = np.zeros((height * width, 3), np.float32)
    objp[:, :2] = np.mgrid[0:width, 0:height].T.reshape(-1, 2)
    objp = objp * square_size
# if square_size is 1.5 centimeters, it would be better to write it as 0.015 meters. Meter is a better metric because most of the time we are working on meter level projects.

# The chessboard is a 9x6 matrix so we set our width=9 and height=6. These numbers are the intersection points square corners met. “Criteria” is our computation criteria to iterate calibration function. You can check OpenCV documentation for the parameters.


