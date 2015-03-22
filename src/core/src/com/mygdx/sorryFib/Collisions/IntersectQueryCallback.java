package com.mygdx.sorryFib;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import finnstr.libgdx.liquidfun.ParticleSystem;

public class IntersectQueryCallback implements QueryCallback {
	public boolean called = false;
	public Fixture fixture;

	@Override 
	public boolean reportFixture(Fixture fixture) {
		called = true;
		this.fixture = fixture;
		System.out.println("Position: " + fixture.getBody().getPosition());
		return false;
	}

	@Override
	public boolean shouldQueryParticleSystem(ParticleSystem p) {
		return false;
	}

	@Override
	public boolean reportParticle(ParticleSystem p, int x) {
		System.out.println("Particle Collision!");
		called = true;
		return false;
	}
}