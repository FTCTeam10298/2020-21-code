# built off this tutorial and a quart of saliva:
# https://pythonprogramming.net/thresholding-image-analysis-python-opencv-tutorial/?completed=/image-arithmetics
# -logic-python-opencv-tutorial/

import cv2
import numpy as np

img = cv2.imread('your-image-here.jpg')
# this is a stack, the goal is to simplify the image by first blasting everything and then grayscaling it.
retval, threshold = cv2.threshold(img, 150, 255, cv2.THRESH_BINARY)
grayscaled = cv2.cvtColor(threshold, cv2.COLOR_BGR2GRAY)
cv2.imshow('original', img)
cv2.imshow('threshold', threshold)
cv2.imshow('blasted', grayscaled)
cv2.waitKey(0)
cv2.destroyAllWindows()
