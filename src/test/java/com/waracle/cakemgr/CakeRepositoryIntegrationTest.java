package com.waracle.cakemgr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.waracle.cakemgr.data.CakeEntity;
import com.waracle.cakemgr.data.CakeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CakeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CakeRepository cakeRepository;

    @Before
    public void resetDb() {
        cakeRepository.deleteAll();
    }


    @Test
    public void whenFindByValidName_thenReturnCake() {
        CakeEntity cake = createCake("persistedcake", "testdesc", "http://testimage.jpg");
        entityManager.persistAndFlush(cake);

        List<CakeEntity> found = cakeRepository.findByTitle(cake.getTitle());
        assertThat(found).extracting(CakeEntity::getTitle).contains("persistedcake");

    }

    @Test
    public void whenInvalidName_thenReturnNull() {
        List<CakeEntity> fromDb = cakeRepository.findByTitle("doesNotExist");
        assertThat(fromDb).isEmpty();
    }

    @Test
    public void whenFindByValidId_thenReturnCake() {
        CakeEntity cake = createCake("idcake", "iddesc", "http://idimage.jpg");

        entityManager.persistAndFlush(cake);
        CakeEntity fromDb = cakeRepository.findById(cake.getCakeId()).orElse(null);
        assertThat(fromDb.getTitle()).isEqualTo(cake.getTitle());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        CakeEntity fromDb = cakeRepository.findById(-2l).orElse(null);
        assertThat(fromDb).isNull();
    }


    private CakeEntity createCake(String title, String desc, String image) {
        CakeEntity cake = new CakeEntity();
        cake.setTitle(title);
        cake.setDescription(desc);
        cake.setImage(image);
        return cake;
    }
}