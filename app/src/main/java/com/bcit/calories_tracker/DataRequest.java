package com.bcit.calories_tracker;

import java.util.List;

public interface DataRequest {
    void onDataReceived(Meal[] meals);
}
