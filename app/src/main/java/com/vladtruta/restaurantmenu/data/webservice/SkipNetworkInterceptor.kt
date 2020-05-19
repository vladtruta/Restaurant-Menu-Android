/*
 * Copyright (C) 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vladtruta.restaurantmenu.data.webservice

import com.google.gson.Gson
import com.vladtruta.restaurantmenu.data.model.local.Category
import com.vladtruta.restaurantmenu.data.model.local.MenuCourse
import com.vladtruta.restaurantmenu.utils.Constants
import okhttp3.*
import java.net.HttpURLConnection

private val categories = listOf(
    Category(Constants.MenuCategories.STARTERS),
    Category(Constants.MenuCategories.MAIN_COURSES),
    Category(Constants.MenuCategories.DESSERTS),
    Category(Constants.MenuCategories.BEVERAGES)
)

private val menuCourses = listOf(
    MenuCourse(
        Category(Constants.MenuCategories.MAIN_COURSES),
        "Chilli con carne",
        "A spicy stew containing chili peppers, meat, and often tomatoes and beans. ",
        "https://food-images.files.bbci.co.uk/food/recipes/chilliconcarne_67875_16x9.jpg",
        "300 g",
        25
    ),
    MenuCourse(
        Category(Constants.MenuCategories.MAIN_COURSES),
        "Chicken tikka masala",
        "Chicken tikka masala is an Indian dish consisting of marinated pieces of chicken breast that are first strung on a skewer and then roasted in a tandoor oven until they are crunchy on the outside and tender on the inside. The pieces of chicken are then braised in a creamy tomato sauce with paprika. \n" +
                "\n" +
                "Chicken tikka masala owes its origins to the world famous dish tandoori chicken as the dish was conceived as a way to make a meal out of leftovers. Since tandoori chicken is served in India without sauce, the next day the meat is usually tough and dried out. However, by stewing the chicken in a creamy tomato sauce full of spices, Indian chefs got the leftovers juicy and tasty again. Chicken tikka masala is actually the sister dish of butter chicken, which was also designed to preserve tandoori meat and keep it tender. \n" +
                "\n" +
                "This leftover dish became so popular that people stopped using the leftovers and started using fresh chicken instead. The marinade used for chicken tikka is the same as for tandoori chicken and other dishes from the tandoor oven.",
        "https://homecookingadventure.com/images/recipes/chicken-tikka-masala-main.jpg",
        "450 g",
        28
    ),
    MenuCourse(
        Category(Constants.MenuCategories.MAIN_COURSES),
        "Roasted pork tenderloin",
        "A cut of meat from a pig, created from the tissue along the dorsal side of the rib cage.",
        "https://cdn-image.foodandwine.com/sites/default/files/styles/medium_2x/public/201307-xl-spice-roasted-pork-tenderloin.jpg?itok=VoUYF6Eq",
        "500 g",
        37
    ),
    MenuCourse(
        Category(Constants.MenuCategories.STARTERS),
        "Eggs Benedict",
        "An American breakfast or brunch dish that consists of two halves of an English muffin topped with a poached egg, bacon or ham, and hollandaise sauce.",
        "https://prods3.imgix.net/images/articles/2017_08/Non-Feature-eggs-benedict-recipe-breakfast1.jpg",
        "180 g",
        14
    ),
    MenuCourse(
        Category(Constants.MenuCategories.DESSERTS),
        "Chocolate cake",
        "A cake flavored with melted chocolate, cocoa powder, or both.",
        "https://food-images.files.bbci.co.uk/food/recipes/easy_chocolate_cake_31070_16x9.jpg",
        "180 g",
        16
    ),
    MenuCourse(
        Category(Constants.MenuCategories.DESSERTS),
        "Strawberry pudding",
        "A simple glazed strawberry mixture is layered with creamy pudding, whipped cream, and vanilla wafer cookies.",
        "https://3.bp.blogspot.com/-nVmA43OFeGk/W3TjRApNPhI/AAAAAAAApLM/yjSlIhq0E34X07L1jroRG2qENlhC1ljnACLcBGAs/s1600/Strawberry-Pudding-Photo.JPG",
        "145 g",
        8
    ),
    MenuCourse(
        Category(Constants.MenuCategories.BEVERAGES),
        "Mineral water",
        "Plain and simple mineral water",
        "https://www.healthxchange.sg/sites/hexassets/Assets/women/urinary-stones-drink-sparkling-mineral-water.jpg",
        "500 ml",
        6
    ),
    MenuCourse(
        Category(Constants.MenuCategories.BEVERAGES),
        "Sauvignon blanc",
        "A green-skinned grape variety that originates from the Bordeaux region of France.",
        "https://static.independent.co.uk/s3fs-public/thumbnails/image/2018/05/02/12/best-sauvignon-blanc.jpg?w968h681",
        "180 ml",
        12
    ),
    MenuCourse(
        Category(Constants.MenuCategories.BEVERAGES),
        "Craft beer",
        "A beer made in a traditional or non-mechanized way by a small brewery.",
        "https://static.vinepair.com/wp-content/uploads/2018/06/beer-bubble-cover-mobile.jpg",
        "400 ml",
        10
    ),
    MenuCourse(
        Category(Constants.MenuCategories.BEVERAGES),
        "Orange juice",
        "A liquid extract of the orange tree fruit, produced by squeezing or reaming oranges.",
        "https://www.earthfoodandfire.com/wp-content/uploads/2018/04/Homemade-Orange-Juice.jpg",
        "350 ml",
        11
    ),
    MenuCourse(
        Category(Constants.MenuCategories.BEVERAGES),
        "Mountain Dew",
        "A carbonated soft drink brand produced and owned by PepsiCo. The original formula was invented in 1940 by beverage bottlers Barney and Ally Hartman.",
        "https://radioimg.s3.amazonaws.com/wycdfm/styles/delta__775x515/s3/Mountain_Dew.jpg?itok=9MtiFKbH",
        "500 ml",
        7
    ),
    MenuCourse(
        Category(Constants.MenuCategories.BEVERAGES),
        "Piña Colada",
        "A sweet cocktail made with rum, cream of coconut or coconut milk, and pineapple juice, usually served either blended or shaken with ice.",
        "https://www.thespruceeats.com/thmb/cDQK02EZOGbvxzBzV_u7-2-woTk=/4494x3000/filters:fill(auto,1)/virgin-pina-colada-recipe-2097115_05-5b0d8124ff1b7800364356ae.jpg",
        "550 ml",
        25
    )
)

class SkipNetworkInterceptor : Interceptor {
    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockForNetworkRequest()

        val request = chain.request()
        if (request.method() == "GET") {
            when (request.url().pathSegments().first()) {
                "categories" -> {
                    return makeOkResult(chain.request(), categories)
                }
                "courses" -> {
                    return makeOkResult(chain.request(), menuCourses)
                }
            }
        }

        return makeErrorResult(chain.request())
    }

    private fun pretendToBlockForNetworkRequest() = Thread.sleep(1000L)

    private fun makeOkResult(request: Request, body: Any): Response {
        return Response.Builder()
            .code(HttpURLConnection.HTTP_OK)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(body)
                )
            )
            .build()
    }

    private fun makeErrorResult(request: Request): Response {
        return Response.Builder()
            .code(HttpURLConnection.HTTP_INTERNAL_ERROR)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Unknown error")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(mapOf("cause" to "not sure"))
                )
            )
            .build()
    }
}