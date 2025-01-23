package ir.dunijet.ghaza.mainScreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.dunijet.ghaza.model.Food
import ir.dunijet.ghaza.model.MyDatabase
import ir.dunijet.ghaza.utils.BASE_URL_IMAGE
import ir.dunijet.ghaza.utils.showToast
import ir.dunijet.ghaza.databinding.ActivityMainBinding
import ir.dunijet.ghaza.databinding.DialogAddNewItemBinding
import ir.dunijet.ghaza.databinding.DialogDeleteItemBinding
import ir.dunijet.ghaza.databinding.DialogUpdateItemBinding

class MainScreenActivity : AppCompatActivity(), FoodAdapter.FoodEvents, MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: FoodAdapter
    private lateinit var presenter: MainScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = MainScreenPresenter(MyDatabase.getDatabase(this).foodDao)

        val sharedPreferences = getSharedPreferences("duniFood", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            presenter.firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }

        presenter.onAttach(this)

        binding.btnRemoveAllFoods.setOnClickListener {
            presenter.onDeleteAllClicked()
        }

        binding.btnAddNewFood.setOnClickListener {
            addNewFood()
        }

        binding.edtSearch.addTextChangedListener { editTextInput ->
            presenter.onSearchFood(editTextInput.toString())
        }



    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
    private fun addNewFood() {

        val dialog = AlertDialog.Builder(this).create()

        val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.dialogBtnDone.setOnClickListener {

            if (
                dialogBinding.dialogEdtNameFood.length() > 0 &&
                dialogBinding.dialogEdtFoodCity.length() > 0 &&
                dialogBinding.dialogEdtFoodPrice.length() > 0 &&
                dialogBinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = dialogBinding.dialogEdtNameFood.text.toString()
                val txtPrice = dialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = dialogBinding.dialogEdtFoodDistance.text.toString()
                val txtCity = dialogBinding.dialogEdtFoodCity.text.toString()
                val txtRatingNumber: Int = (1..150).random()
                val ratingBarStar: Float = (1..5).random().toFloat()
                val randomForUrl = (1 until 12).random()
                val urlPic = BASE_URL_IMAGE + randomForUrl.toString() + ".jpg"



                val newFood = Food(
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = urlPic,
                    numOfRating = txtRatingNumber,
                    rating = ratingBarStar
                )

                presenter.onAddNewFoodClicked(newFood)
                dialog.dismiss()

            } else {
                showToast("لطفا همه مقادیر را وارد کنید :)")
            }

        }


    }

    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)

        updateDialogBinding.dialogEdtNameFood.setText(food.txtSubject)
        updateDialogBinding.dialogEdtFoodCity.setText(food.txtCity)
        updateDialogBinding.dialogEdtFoodPrice.setText(food.txtPrice)
        updateDialogBinding.dialogEdtFoodDistance.setText(food.txtDistance)

        dialog.setView(updateDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        updateDialogBinding.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        updateDialogBinding.dialogUpdateBtnDone.setOnClickListener {

            if (
                updateDialogBinding.dialogEdtNameFood.length() > 0 &&
                updateDialogBinding.dialogEdtFoodCity.length() > 0 &&
                updateDialogBinding.dialogEdtFoodPrice.length() > 0 &&
                updateDialogBinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = updateDialogBinding.dialogEdtNameFood.text.toString()
                val txtPrice = updateDialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = updateDialogBinding.dialogEdtFoodDistance.text.toString()
                val txtCity = updateDialogBinding.dialogEdtFoodCity.text.toString()

                val newFood = Food(
                    id = food.id,
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = food.urlImage,
                    numOfRating = food.numOfRating,
                    rating = food.rating
                )

                presenter.onUpdateFood(newFood , position)
                dialog.dismiss()

            } else {
                showToast("لطفا همه مقادیر را وارد کن :)")
            }

        }

    }
    override fun onFoodLongClicked(food: Food, pos: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogDeleteBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogDeleteBinding.dialogBtnDeleteCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogDeleteBinding.dialogBtnDeleteSure.setOnClickListener {

            presenter.onDeleteFood(food , pos)
            dialog.dismiss()

        }

    }

    override fun showFoods(data: List<Food>) {
        mainAdapter = FoodAdapter(ArrayList(data), this)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }
    override fun refreshFoods(data: List<Food>) {
        mainAdapter.setData(ArrayList(data))
    }
    override fun addNewFood(newFood: Food) {
        mainAdapter.addFood(newFood)
    }
    override fun deleteFood(oldFood: Food, pos: Int) {
        mainAdapter.removeFood(oldFood, pos)
    }
    override fun updateFood(editingFood: Food, pos: Int) {
        mainAdapter.updateFood(editingFood, pos)
    }

}