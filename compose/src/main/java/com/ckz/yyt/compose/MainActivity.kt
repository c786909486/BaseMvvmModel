package com.ckz.yyt.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ckz.library_base_compose.AppNav
import com.ckz.library_base_compose.base.BaseComposeActivity
import com.ckz.yyt.compose.ui.MyCompseViewModel
import com.ckz.yyt.compose.ui.theme.BaseMvvmModelTheme

class MainActivity : BaseComposeActivity() {

    override val lifecycle: Lifecycle
        get() = super.lifecycle
    @Composable
    override fun initPage() {
        BaseMvvmModelTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                AppNav.instance.AppHost(
                    navController = navController,
                    startDestination = "home",

                ){
                    composable("home"){
                        Greeting("Android")
                    }

                    composable("second"){
                        SecondPage()
                    }


                }
            }
        }
    }


}

@Composable
fun SecondPage(){
    Column {
        Text(text = "第二个页面")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val myViewModel:MyCompseViewModel = viewModel()
//    Text(
//        text = "Hello $name!",
//        modifier = modifier.clickable {
//            myViewModel.showEmpty("123")
//
//        }
//    )
    myViewModel.ContentWidget {
        Column {
            Text(text = "123123", modifier = Modifier.clickable {
                myViewModel.showToast("2312312")
            })

            Button(onClick = { AppNav.instance.navController.navigate("second") }) {
                Text(text = "secondPage")
            }

            Button(onClick = {
                myViewModel.showProcessDialog(msg = "登录。。。")
            }) {
                Text(text = "加载弹窗")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaseMvvmModelTheme {
        Greeting("Android")
    }
}