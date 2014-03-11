package com.github.dandelion.core.bundle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.dandelion.core.asset.Asset;
import com.github.dandelion.core.asset.AssetType;

public class BundleStorageTest1 {

	private BundleStorage bundleStorage;

	@Before
	public void setup() {
		List<Bundle> bundlesToAdd = new LinkedList<Bundle>();

		Set<Asset> assets1 = new LinkedHashSet<Asset>();
		assets1.add(new Asset("asset1_1", "1.0.0", AssetType.js));
		assets1.add(new Asset("asset1_2", "1.0.0", AssetType.js));
		Bundle b1 = new Bundle("b1", assets1);

		Set<Asset> assets2 = new LinkedHashSet<Asset>();
		assets2.add(new Asset("asset2_1", "1.0.0", AssetType.js));
		Bundle b2 = new Bundle("b2", assets2);
		b2.addDependency("b1");

		Set<Asset> assets3 = new LinkedHashSet<Asset>();
		assets3.add(new Asset("asset3_1", "1.0.0", AssetType.js));
		assets3.add(new Asset("asset3_2", "1.0.0", AssetType.css));
		assets3.add(new Asset("asset3_3", "1.0.0", AssetType.css));
		Bundle b3 = new Bundle("b3", assets3);
		b3.addDependency("b1");

		Set<Asset> assets4 = new LinkedHashSet<Asset>();
		assets4.add(new Asset("asset4_1", "1.0.0", AssetType.css));
		Bundle b4 = new Bundle("b4", assets4);
		b4.addDependency("b2");
		b4.addDependency("b3");

		bundlesToAdd.add(b3);
		bundlesToAdd.add(b2);
		bundlesToAdd.add(b1);
		bundlesToAdd.add(b4);

		bundleStorage = new BundleStorage();
		bundleStorage.loadBundles(bundlesToAdd);
	}

	@Test
	public void should_have_set_the_right_edges() {
		assertThat(bundleStorage.getBundleDag().hasEdge("b2", "b1"));
		assertThat(bundleStorage.getBundleDag().hasEdge("b3", "b1"));
		assertThat(bundleStorage.getBundleDag().hasEdge("b4", "b2"));
		assertThat(bundleStorage.getBundleDag().hasEdge("b4", "b3"));
	}

	@Test
	public void should_return_all_necessary_bundles_when_requesting_b1() {
		List<String> bundlesToLoad = bundleStorage.getBundleDag().bundlesFor("b1");
		assertThat(bundlesToLoad).containsExactly("b1");
	}

	@Test
	public void should_return_all_necessary_bundles_when_requesting_b2() {
		List<String> bundlesToLoad = bundleStorage.getBundleDag().bundlesFor("b2");
		assertThat(bundlesToLoad).containsExactly("b1", "b2");
	}

	@Test
	public void should_return_all_necessary_bundles_when_requesting_b3() {
		List<String> bundlesToLoad = bundleStorage.getBundleDag().bundlesFor("b3");
		assertThat(bundlesToLoad).containsExactly("b1", "b3");
	}

	@Test
	public void should_return_all_necessary_bundles_when_requesting_b4() {
		List<String> bundlesToLoad = bundleStorage.getBundleDag().bundlesFor("b4");
		assertThat(bundlesToLoad).containsExactly("b1", "b2", "b3", "b4");
	}
}