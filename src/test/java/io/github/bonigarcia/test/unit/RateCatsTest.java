/*
 * (C) Copyright 2017 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.test.unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.aspectj.weaver.Lint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.bonigarcia.Cat;
import io.github.bonigarcia.CatException;
import io.github.bonigarcia.CatRepository;
import io.github.bonigarcia.CatService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (black-box): rating cats")
@Tag("unit")
class RateCatsTest {

    @InjectMocks
    CatService catService;

    @Mock
    CatRepository catRepository;

    // Test data
    Cat dummy = new Cat("dummy", "dummy.png");
    int stars = 5;
    String comment = "foo";
    int negStars = -1;

    @ParameterizedTest(name = "Rating cat with {0} stars")
    @ValueSource(doubles = { 0.5, 5 })
    @DisplayName("Correct range of stars test")
    @Tag("functional-requirement-3")
    void testCorrectRangeOfStars(double stars) {
        Cat dummyCat = catService.rateCat(stars, dummy);
        assertThat(dummyCat.getAverageRate(), equalTo(stars));
    }

    @ParameterizedTest(name = "Rating cat with {0} stars")
    @ValueSource(ints = { 0, 6 })
    @DisplayName("Incorrect range of stars test")
    @Tag("functional-requirement-3")
    void testIncorrectRangeOfStars(int stars) {
        assertThrows(CatException.class, () -> {
            catService.rateCat(stars, dummy);
        });
    }

    @Test
    @DisplayName("Rating cats with a comment")
    @Tag("functional-requirement-4")
    void testRatingWithComments() {
        when(catRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dummy));
        Cat dummyCat = catService.rateCat(stars, comment, 0);
        assertThat(
                catService.getOpinions(dummyCat).iterator().next().getComment(),
                equalTo(comment));
    }

    @Test
    @DisplayName("Rating cats with empty comment")
    @Tag("functional-requirement-4")
    void testRatingWithEmptyComments() {
        when(catRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dummy));
        Cat dummyCat = catService.rateCat(stars, dummy);
        assertThat(
                catService.getOpinions(dummyCat).iterator().next().getComment(),
                emptyString());
    }

    //Test unitarios


    @ParameterizedTest(name = "Poner nota negativa")
    @ValueSource(ints = { -5 })
    @Test
    @DisplayName("Poner nota con estrellas negativas")
    @Tag("functional-requirement-3")
    void testNegativeStars() {
        assertThrows(CatException.class, () -> {
            catService.rateCat(-5, dummy);
        });
    }





    @Test
    @DisplayName("Contar cantidad de gatos")
    @Tag("functional-requirement-5")
    void testCountCats(){
        Cat dummyCat = catService.rateCat(stars, dummy);
        catService.saveCat(dummyCat);
        List<Cat> gatos = new ArrayList<>();
        gatos.add(dummyCat);

        when(catRepository.findAll())
                .thenReturn(gatos);
       
        assertEquals(
            1,
            catService.getCatCount());
    }

    

    @Test
    @DisplayName("Obtener todos los gatos")
    @Tag("functional-requirement-6")
    void testTodosLosGatos(){
        Cat dummyCat = catService.rateCat(stars, dummy);
        catService.saveCat(dummyCat);
        List<Cat> gatos = new ArrayList<>();
        gatos.add(dummyCat);
        when(catRepository.findAll())
                .thenReturn(gatos);

        assertEquals(
            catService.getAllCats(),
            gatos);       
    }


    

    @Test
    @DisplayName("Obtener la nota promedio")
    @Tag("functional-requirement-8")
    void testNotaPromedio(){

        Cat dummyCat = catService.rateCat(stars, dummy);

        assertEquals(
            dummyCat.getAverageRate(),
            5);       
    }

    @Test
    @DisplayName("Obtener la nota promedio como string")
    @Tag("functional-requirement-9")
    void testNotaPromedioString(){

        Cat dummyCat = catService.rateCat(stars, dummy);

        assertEquals(
            "5.00",
            dummyCat.getAverageRateAsString());       
    }

    @Test
    @DisplayName("Obtener la nota promedio redondeada")
    @Tag("functional-requirement-10")
    void testNotaPromedioRedondeada(){

        Cat dummyCat = catService.rateCat(stars, dummy);

        assertEquals(
            5.00,
            dummyCat.getHalfRoundedAverageRate());       
    }



}
