package com.example.csc207courseproject.ui;

import android.content.Context;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.csc207courseproject.databinding.FragmentSeedingBinding;
import org.jetbrains.annotations.NotNull;

public abstract class AppFragment extends Fragment {
    protected FragmentSeedingBinding binding;
    protected Context mContext;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    protected void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
