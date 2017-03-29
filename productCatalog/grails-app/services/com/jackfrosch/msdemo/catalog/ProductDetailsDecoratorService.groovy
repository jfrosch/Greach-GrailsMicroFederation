package com.jackfrosch.msdemo.catalog

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

@Transactional
class ProductDetailsDecoratorService {

    List<Product> findRecommendations(Product product) {
        RestBuilder rest = new RestBuilder()
        def resp = rest.get("http://localhost:8092/findRecommendation/${product.id}")

        if(resp.status == 500) {
            throw new RuntimeException("Error calling recommendation engine")
        }
        List recommendedProductIds = resp.json.recommendedProducts.collect { it.id as Long } ?: []

        recommendedProductIds ? Product.where { id in recommendedProductIds}.list() : []
    }

}
